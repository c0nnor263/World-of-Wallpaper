package com.doodle.baselineprofile

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This test class benchmarks the speed of app startup.
 * Run this benchmark to verify how effective a Baseline Profile is.
 * It does this by comparing [CompilationMode.None], which represents the app with no Baseline
 * Profiles optimizations, and [CompilationMode.Partial], which uses Baseline Profiles.
 *
 * Run this benchmark to see startup measurements and captured system traces for verifying
 * the effectiveness of your Baseline Profiles. You can run it directly from Android
 * Studio as an instrumentation test, or run all benchmarks with this Gradle task:
 * ```
 * ./gradlew :baselineprofile:connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.androidx.benchmark.enabledRules=Macrobenchmark
 * ```
 *
 * You should run the benchmarks on a physical device, not an Android emulator, because the
 * emulator doesn't represent real world performance and shares system resources with its host.
 *
 * For more information, see the [Macrobenchmark documentation](https://d.android.com/macrobenchmark#create-macrobenchmark)
 * and the [instrumentation arguments documentation](https://d.android.com/topic/performance/benchmarking/macrobenchmark-instrumentation-args).
 **/

@LargeTest
@RunWith(AndroidJUnit4::class)
class StartupMeasureBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startupCompilationNone() = startup(CompilationMode.None())

    @Test
    fun startupPartialWithBaselineProfiles() =
        startup(CompilationMode.Partial(baselineProfileMode = BaselineProfileMode.Require))

    @Test
    fun startupPartialCompilation() = startup(
        CompilationMode.Partial(
            baselineProfileMode = BaselineProfileMode.Disable,
            warmupIterations = 3
        )
    )

    @Test
    fun startupCompilationFull() = startup(CompilationMode.Full())

    private fun startup(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "com.doodle.turboracing3",
        metrics = listOf(StartupTimingMetric(), FrameTimingMetric()),
        iterations = 10,
        compilationMode = compilationMode,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()
    }
}
