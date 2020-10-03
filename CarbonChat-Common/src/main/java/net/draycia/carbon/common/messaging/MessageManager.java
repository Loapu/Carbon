package net.draycia.carbon.common.messaging;

import net.draycia.carbon.api.CarbonChat;
import net.draycia.carbon.api.channels.TextChannel;
import net.draycia.carbon.api.messaging.MessageService;
import com.google.common.io.ByteArrayDataOutput;
import net.draycia.carbon.api.channels.ChatChannel;
import net.draycia.carbon.api.users.ChatUser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;
import java.util.function.Consumer;

public class MessageManager {

  private @NonNull final CarbonChat carbonChat;

  private @NonNull final MessageService messageService;

  public MessageManager(@NonNull final CarbonChat carbonChat, @NonNull final MessageService messageService) {
    this.carbonChat = carbonChat;
    this.messageService = messageService;

    this.registerDefaultListeners();
  }

  private void registerDefaultListeners() {
    this.messageService().registerUserMessageListener("nickname", (user, byteArray) -> {
      final String nickname = byteArray.readUTF();
      final String message = this.carbonChat.translations().nicknameSet();

      user.nickname(nickname, true);
      user.sendMessage(this.carbonChat.messageProcessor().processMessage(message, "nickname", nickname));
    });

    this.messageService().registerUserMessageListener("nickname-reset", (user, byteArray) -> {
      final String message = this.carbonChat.translations().nicknameReset();

      user.nickname(null, true);
      user.sendMessage(this.carbonChat.messageProcessor().processMessage(message));
    });

    this.messageService().registerUserMessageListener("selected-channel", (user, byteArray) -> {
      user.selectedChannel(this.carbonChat.channelRegistry().get(byteArray.readUTF()), true);
    });

    this.messageService().registerUserMessageListener("spying-whispers", (user, byteArray) -> {
      user.spyingWhispers(byteArray.readBoolean(), true);
    });

    this.messageService().registerUserMessageListener("muted", (user, byteArray) -> {
      user.muted(byteArray.readBoolean(), true);
    });

    this.messageService().registerUserMessageListener("shadow-muted", (user, byteArray) -> {
      user.shadowMuted(byteArray.readBoolean(), true);
    });

    this.messageService().registerUserMessageListener("reply-target", (user, byteArray) -> {
      user.replyTarget(new UUID(byteArray.readLong(), byteArray.readLong()), true);
    });

    this.messageService().registerUserMessageListener("ignoring-user", (user, byteArray) -> {
      user.ignoringUser(new UUID(byteArray.readLong(), byteArray.readLong()), byteArray.readBoolean(), true);
    });

    this.messageService().registerUserMessageListener("ignoring-channel", (user, byteArray) -> {
      user.channelSettings(this.carbonChat.channelRegistry().get(byteArray.readUTF()))
        .ignoring(byteArray.readBoolean(), true);
    });

    this.messageService().registerUserMessageListener("spying-channel", (user, byteArray) -> {
      user.channelSettings(this.carbonChat.channelRegistry().get(byteArray.readUTF()))
        .spying(byteArray.readBoolean(), true);
    });

    this.messageService().registerUserMessageListener("channel-color", (user, byteArray) -> {
      user.channelSettings(this.carbonChat.channelRegistry().get(byteArray.readUTF()))
        .color(TextColor.fromHexString(byteArray.readUTF()), true);
    });

    this.messageService().registerUserMessageListener("channel-color-reset", (user, byteArray) -> {
      user.channelSettings(this.carbonChat.channelRegistry().get(byteArray.readUTF()))
        .color(null, true);
    });

    this.messageService().registerUUIDMessageListener("channel-component", (uuid, byteArray) -> {
      final ChatChannel channel = this.carbonChat.channelRegistry().get(byteArray.readUTF());
      final ChatUser user = this.carbonChat.userService().wrap(uuid);

      if (channel instanceof TextChannel) {
        final Component component = this.carbonChat.gsonSerializer().deserialize(byteArray.readUTF());

        ((TextChannel) channel).sendComponent(user, component);

        this.carbonChat.messageProcessor().audiences().console().sendMessage(component);
      }
    });

    this.messageService().registerUUIDMessageListener("whisper-component", (uuid, byteArray) -> {
      final UUID recipient = new UUID(byteArray.readLong(), byteArray.readLong());

      final ChatUser target = this.carbonChat.userService().wrap(recipient);
      final String message = byteArray.readUTF();

      if (!target.ignoringUser(uuid)) {
        target.replyTarget(uuid);
        target.sendMessage(this.carbonChat.gsonSerializer().deserialize(message));
      }
    });
  }

  public @NonNull MessageService messageService() {
    return this.messageService;
  }

  public void sendMessage(@NonNull final String key, @NonNull final UUID uuid,
                          @NonNull final Consumer<@NonNull ByteArrayDataOutput> consumer) {
    this.messageService().sendMessage(key, uuid, consumer);
  }

}
