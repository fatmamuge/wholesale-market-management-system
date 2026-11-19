#!/bin/bash

./gradlew installDebug
adb shell am start -n com.fatmamuge.halyonetim/.MainActivity
