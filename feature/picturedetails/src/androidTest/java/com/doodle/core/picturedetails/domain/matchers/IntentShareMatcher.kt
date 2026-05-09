package com.doodle.core.picturedetails.domain.matchers

import android.content.Intent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher

class IntentShareMatcher : TypeSafeMatcher<Intent>() {
    override fun describeTo(description: org.hamcrest.Description?) {
        description?.appendText("Share Intent")
    }

    override fun matchesSafely(item: Intent?): Boolean {
        return item?.action == Intent.ACTION_SEND &&
            item.type == "text/plain"
    }
}

fun chooser(matcher: TypeSafeMatcher<Intent>): Matcher<Intent> {
    return allOf(
        hasAction(Intent.ACTION_CHOOSER),
        hasExtra(`is`(Intent.EXTRA_INTENT), matcher)
    )
}
