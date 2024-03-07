architectury {
	common {
		for(p in rootProject.subprojects) {
			if(p != project) {
				this@common.add(p.name)
			}
		}
	}
}

dependencies {
	// We depend on fabric loader here to use the fabric @Environment annotations and get the mixin dependencies
	// Do NOT use other classes from fabric loader
	modImplementation("net.fabricmc:fabric-loader:${"fabric_loader_version"()}")
	// Compile against Create Fabric in common
	// beware of differences across platforms!
	// dependencies must also be pulled in to minimize problems, from remapping issues to compile errors.
	// All dependencies except Flywheel and Registrate are NOT safe to use!
	// Flywheel and Registrate must also be used carefully due to differences.
	modCompileOnly("com.simibubi.create:create-fabric-${"minecraft_version"()}:${"create_fabric_version"()}+mc${"minecraft_version"()}") {
		exclude(group = "com.github.llamalad7.mixinextras", module = "mixinextras")
	}

	// required for proper remapping and compiling
	modCompileOnly("net.fabricmc.fabric-api:fabric-api:${"fabric_api_version"()}+${"minecraft_version"()}")

	annotationProcessor(implementation("io.github.llamalad7:mixinextras-common:${"mixin_extras_version"()}")!!)
}

tasks.processResources {
	// don't add development or to-do files into built jar
	exclude("**/*.bbmodel", "**/*.lnk", "**/*.xcf", "**/*.md", "**/*.txt", "**/*.blend", "**/*.blend1")

	// exclude create assets
	exclude("src/main/resources/assets/create/**")
}

sourceSets.main {
	resources { // include generated resources in resources
		srcDir("src/generated/resources")
		exclude(".cache/**")
		exclude("assets/create/**")
	}
}

operator fun String.invoke(): String {
	return rootProject.ext[this] as? String
		?: throw IllegalStateException("Property $this is not defined")
}