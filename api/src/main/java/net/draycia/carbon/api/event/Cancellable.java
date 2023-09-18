/*
 * CarbonChat
 *
 * Copyright (c) 2023 Josua Parks (Vicarious)
 *                    Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.draycia.carbon.api.event;

/**
 * Marks an event as cancellable.
 *
 * @since 2.1.0
 */
public interface Cancellable {

    /**
     * Gets if the event is cancelled.
     *
     * @return if the event is cancelled
     * @since 2.1.0
     */
    boolean cancelled();

    /**
     * Sets the cancelled state.
     *
     * @param cancelled new cancelled state
     * @since 2.1.0
     */
    void cancelled(boolean cancelled);

}