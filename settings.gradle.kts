plugins {
	id("com.gradle.enterprise").version("3.1.1")
}

val regex = Regex("""    \w*\("([^,]*)"\).*""")
File(rootDir, "buildSrc/src/main/kotlin/com/jdroid/android/Module.kt").forEachLine { line ->
	val matchResult = regex.matchEntire(line)
	if (matchResult != null && matchResult.groupValues.size > 1) {
		val module = matchResult.groupValues[1]
		include(":$module")
	}
}

apply(from = File(settingsDir, "buildCacheSettings.gradle"))
