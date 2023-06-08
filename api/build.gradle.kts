plugins {
  id("carbon.publishing-conventions")
}

dependencies {
  // Doesn't add any dependencies, only version constraints
  api(platform(libs.adventureBom))
  api(platform(libs.log4jBom))

  // Provided by platform
  compileOnlyApi(libs.adventureApi)
  compileOnlyApi(libs.adventureTextSerializerPlain)
  compileOnlyApi(libs.adventureTextSerializerLegacy)
  compileOnlyApi(libs.adventureTextSerializerGson) {
    exclude("com.google.code.gson")
  }
  compileOnlyApi(libs.minimessage)

  compileOnlyApi(libs.checkerQual)

  api(libs.event)
  api(libs.kyoriRegistry) {
    exclude("com.google.guava")
  }
  api(libs.kyoriMoonshine)
  api(libs.kyoriMoonshineCore)
  api(libs.kyoriMoonshineStandard)

  // we shade and relocate a newer version than minecraft provides
  compileOnlyApi(libs.guava)

  // Provided by Minecraft
  compileOnlyApi(libs.gson)
  compileOnlyApi(libs.log4jApi)

  api(libs.messenger)
}
