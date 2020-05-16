repositories {
    jcenter()
}

plugins {
    `kotlin-dsl`
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.5"
}
