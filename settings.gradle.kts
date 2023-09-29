
rootProject.name = "bip-crypto"

sourceControl {
    gitRepository(uri("https://github.com/angelos-project/angelos-project-crypt.git")) {
        producesModule("angelos-project-crypt:org.angproj.crypt")
    }
    gitRepository(uri("https://github.com/angelos-project/angelos-project-aux.git")) {
        producesModule("angelos-project-aux:org.angproj.aux.util")
    }
}