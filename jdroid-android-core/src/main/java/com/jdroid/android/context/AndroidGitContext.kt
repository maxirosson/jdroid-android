package com.jdroid.android.context

import com.jdroid.java.context.GitContext

class AndroidGitContext : AbstractAppContext(), GitContext {

    override fun getBranch(): String? {
        return getBuildConfigValue<String>("GIT_BRANCH", null)
    }

    override fun getSha(): String? {
        return getBuildConfigValue<String>("GIT_SHA", null)
    }
}
