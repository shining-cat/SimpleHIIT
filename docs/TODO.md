# SimpleHIIT ToDo list

## Publication
* Find how to publish: Fdroid, or github, or home?

## General technical improvements
* could we extract even more **platform-agnostic** logic from the viewmodels?
* add tests on viewmodels after having tried to make them lighter
* use new [compose stability plugin](https://proandroiddev.com/compose-stability-analyzer-real-time-stability-insights-for-jetpack-compose-1399924a0a64) to review composables and eventually optimise them further (also gradle plugin)
* screenshot tests if they can run on github for free
* improve ui arrangement bucketing: very large displays in portrait are rendered as horizontal and it doesn't look very good

## Form factors (phone - AndroidTV - smartWatch)
* move the whole thing to KMP and build other platforms
* watch? check [Google sample for Watch](https://github.com/android/wear-os-samples/tree/main/WearVerifyRemoteApp)

## Miscellaneous / nice to have
* user object could expose the timestamp of their last session so we could sort them on this on the home for convenience
* When user unselect ALL exercise types, allow the session to still run, without showing any
  exercise, as a timer only. Show a message instead of the missing gifs.
* create a _About_ section, in which to add credits for PoseMy.Art, it could also hold the
  hiit_description, accessible through a new ? button in the home top app bar actions menu
* refine fr and swedish translations
* statistics maybe remove seconds from displays to reduce clutter? (maybe when a value is over 1h)
