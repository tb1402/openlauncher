![graphic](https://raw.githubusercontent.com/OpenLauncherTeam/openlauncher/master/fastlane/metadata/android/en-US/images/featureGraphic.png)

### Description

This is an open source launcher project for Android devices that has been built completely from scratch. The main goal of this launcher is to find a healthy medium between customization and simplicity. At this point in time it implements most features required in a typical launcher but could benefit greatly from some general polish. If you would like to help out feel free to submit issues or ask about submitting a pull request with a feature you want to see in the launcher.

<div style="display:flex;">
    <img src="https://raw.githubusercontent.com/OpenLauncherTeam/openlauncher/master/assets/screenshots.png">
</div>

### Changes in this fork

This fork adds some extra settings to lock down the launcher:

- password for settings
- disable pages on desktop
- disable uninstall and app info options
- disable creation of widgets and shortcuts
- lock wifi, bt, gps or airplane mode, so they stay always on or off

### Why I forked openlauncher

I was searching for a launcher which could provide a "kiosk"-mode (a.k.a a strongly locked down launcher which could only be used to open apps) and the ability to hide apps.<br>
After some digging I found [this app](https://play.google.com/store/apps/details?id=com.robomigo.launcher) in the PlayStore, but it isn't open-source and uses firebase/gms ads for tracking. So I decompiled and modified it, until at some point I found the openlaucher GitHub link in the strings.xml. Now instead of manually editing the .smali files on the closed source app, I decided to fork openlauncher and add the kiosk features myself.

## Note:

As the primary use case for this fork is the usage on some of my personal devices, I do not plan to make it available in any app store or publish apks here.<br>
Opened issues will not be resolved until I see need myself.<br>
Furthermore, to enforce the settings for locking wifi, bt etc. to stay on/off, ADB is needed and only the necessary instructions are given in the app, so you must be familiar with using ADB and I will not provide a full manual for making the settings work.<br>
Nevertheless if you wish to use the app, just build it yourself or contact me for an apk.<br>
Additionally, the minimum android version was set to SDK 21 (Android 5.0) as it was required by the ADB library which I used.

### Status

If your instance is crashing frequently please update the app and reset the data and settings before creating an issue. This project is not actively developed at the moment since all of the main contributors either started working on other projects or find the current state of the launcher sufficient for daily use. If you would like to see a change please realize that it may not get added at all unless someone decides to write the functionality. Pull requests are welcome from anyone! Please ask about large features first, we can help navigate the codebase and talk about where best to add the functionality.

### Features

* Paged desktop
* Dock
* Drag and drop
* Hide apps
* Scrollable background
* Search bar
* Icon packs

### Contributions

The project is always open for contributions and accepts pull requests. Please use the _auto reformat feature_ in Android Studio before sending a pull request. Translations can be contributed on GitHub. You can use Stringlate to translate the project directly on your Android phone. It allows you to post the translations on GitHub with little effort.

### Resources

* Team: [bennykok](https://github.com/BennyKok) | [dkanada](https://github.com/dkanada) | [gsantner](https://gsantner.net/supportme?source=readme&project=openlauncher)
* Project: [Changelog](/CHANGELOG.md) | [License](/LICENSE)
* F-Droid: [Metadata](https://gitlab.com/fdroid/fdroiddata/blob/master/metadata/com.benny.openlauncher.txt) | [Page](https://f-droid.org/packages/com.benny.openlauncher/) | [Wiki](https://f-droid.org/wiki/page/com.benny.openlauncher) | [Build](https://f-droid.org/wiki/page/com.benny.openlauncher/lastbuild)

### Donation

* OpenLauncherTeam: [liberapay](https://liberapay.com/OpenLauncherTeam/donate)
* bennykok: [paypal](https://www.paypal.me/BennyKok) or [liberapay](https://liberapay.com/BennyKok/donate)
* gsantner: [website](https://gsantner.net/supportme?source=readme&project=openlauncher) or [liberapay](https://liberapay.com/gsantner/donate)

### License

The app is licensed with Apache 2.0.
