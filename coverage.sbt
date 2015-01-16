import org.scoverage.coveralls.CoverallsPlugin._
import scoverage.ScoverageSbtPlugin
import scoverage.ScoverageSbtPlugin._

ScoverageSbtPlugin.instrumentSettings

CoverallsPlugin.coverallsSettings

CoverallsKeys.coverallsTokenFile := Some("./token.txt")

ScoverageKeys.minimumCoverage := 70

ScoverageKeys.failOnMinimumCoverage := true

ScoverageKeys.highlighting := true
