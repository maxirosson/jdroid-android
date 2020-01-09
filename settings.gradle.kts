val regex = Regex("\\(\"([^,]*)\"\\)")
File(rootDir, "buildSrc/src/main/kotlin/Module.kt").forEachLine { line ->
	val matchResult = regex.find(line.trim())
	if (matchResult != null && matchResult.groupValues.size > 1) {
		val module = matchResult.groupValues[1]
		include(module)
	}
}

apply(from = File(settingsDir, "buildCacheSettings.gradle"))
