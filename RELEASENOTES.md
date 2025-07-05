# Release Notes

## 1.9.0 - Latest

#### Improvements & Bug Fixes

- Fixes issues with Search functionality enabling users to be found even if marked as inactive by the API
- Also fixes recent searches list to update properly and return more than one person

## 1.8.7

#### Improvements & Bug Fixes

- fixed broken endpoints related to new hero id for Yurei
- add photo and enum for Yurei

## 1.8.6

#### Improvements & Bug Fixes

- Add photo for Boris
- Add photo for Wukong
- Add photo for Aegis of Agawar

## 1.8.5

#### Improvements & Bug Fixes

- Add photo for Yin
- Add photo for Mourn
- Add photo for new items

## 1.8.3

#### Improvements & Bug Fixes

- Add photo for Skyler
- Add photo for new items
- fix some font colors and null references

## 1.8.2

#### New Features

- Console users can now edit their display name for their claimed player
- Users can search for matches now by match ID

## 1.8.1

#### New Features

- Users can now override the device settings for light/dark mode in the profile tab.

#### Improvements & Bug Fixes

- Fixes match history being broken after the 10th match
- Fixes some visual padding in the Profile menu
- Improves VP gain/loss in ranked vs unranked matches
- Fixes broken links to heroes from the Home Screen under "Top Heroes by Win Rate" and "Top Heroes by Pick Rate"

#### New Features

- App can now be used without an account
- User can sign in or create an account from the profile screen
- App has been renamed to "PredCompanion"

#### Improvements & Bug Fixes

- Auth continues to stabilize

## 1.7.5

#### Improvements & Bug Fixes

- Fixes Auth Flow signing peoplem out and crashing the app

## 1.7.0 - Latest

- Adds image for Terra
- Filters builds on Hero Details by latest game version

## 1.6.0

#### New Features

- Latest items have been added to items and builds screen
- Aurora has been added to heroes list
- Builds support and show 6 items

#### Improvements & Bug Fixes

- Adding/Removing Favorite Builds is more responsively reflected on the Home Screen
- Builds screen no longer crashes

## 1.5.0

#### New Features

- Adds Favorite Builds section to Home Screen and ability to favorite builds from the build details screen.
- Adds new console dropdown in Profile screen to select your console. This will show hero abilities with the updated inputs based on the console you select.

## 1.4.3

#### Improvements & Bug Fixes

- Search has been completely reworked to be available on any top level destination (Home, Heroes, Items, Builds, Profile) and the original search screen has been changed to Home in order to show more content
- Fixed some minor animation bugs that weren't firing

## 1.4.2

#### Improvements & Bug Fixes

- Adds filter options to the Builds screen for builds of the most current version of the game
- Adds version badge to build list and build detail page
- Enables Edge to Edge diplay
- Fixes a bug where the app would crash for users on Android 12 and below

## 1.4.1

#### Improvements & Bug Fixes

- Adds photo of GRIM.exe to the hero pool

## 1.4.0

#### New Features

- Adds new widget that users can add to their phones home screen to quickly view their claimed player stats.

## 1.3.2

#### New Features

- Adds top five most popular builds for a hero on the hero details page

#### Improvements & Bug Fixes

- cleans up loading state for search screen

## 1.3.1

#### Improvements & Bug Fixes

- Fixed app crashing from missing SupaBase parameter

## 1.3.0

#### New Features

- Adds Top 5 Win Rate and Pick Rate to Search Screen for Heroes with details screen for all heroes.

#### Improvements & Bug Fixes

- Improves logic for fetching recent players on search screen
- Fixes a bug where total damage to structures was off in the match details screen

## 0.8.0

#### New Features

- Users can now delete their account from the Profile screen

#### Improvements and Bug Fixes

- Search box on Builds tab now works properly and can be used to search for specific builds

## 0.7.3

#### Improvements & Bug Fixes

- Build list now retains its filters and scroll position when navigating back to it
- Fixes theming bug where top bar would be hidden in dark mode (Firebase Testing Only)

## 0.7.2

- Adds photo for Kwang

## 0.7.1 - Latest

#### Improvements & Bug Fixes

- Fixes search input on the search screen not returning searched for users
- Fixes a bug in player details if MMR or rank came back null
- Improves loading of images on the match detail drop down of each player

## 0.7.0

#### New Features

- Builds! The app now contains a builds section for you to lookup builds for your favorite heroes. You can filter by all the same filters available on the website

#### Improvements & Bug Fixes

- Fixed the text color for the "Delete all recent searches" button

#### Coming Soon
- Up/Down Vote on builds
- Hero Stats on Build Details Screen
- Build Creation



## 0.6.6

#### Improvements & Bug Fixes

- [Larger Screens] Changes the Heroes and Items screen to have more entries in each row for larger screens
- App Version is now displayed on the Profile Screen
- Adds better error messaging when the API returns bad data for match details

## 0.6.5

#### New Features

- Adds image for Iggy & Scorch

#### Improvements & Bug Fixes

- Fixes a bug where images wouldn't load on the hero details screen
- Fixes ability text that had HTML tags in it
- Fixes physical power text on the hero details - stats screen

## 0.6.4

- Fix Fetch Logic for Search Screen

## 0.6.3

- Minor improvements to deployment process

## 0.6.2

#### New Features

- Overall win rate and Average Player Score have been added to the Player Details screen
- Made Firebase Feedback quieter


## 0.5.5

#### New Features

- Added buttons to clear individual and all recent searches
- Added caching to some endpoints to allow for offline browsing
- adds some unit tests to add to the CI pipeline (TBD)

#### Improvements & Bug Fixes
- updates stats and rank icon on recent search entries
- fixes the relative timestamps on match history


## 0.5.0 - Latest

#### New Features

- Added Browsable Items Page to view all items in detail
- Added panel in Player Details for stats per hero played
- Adds Player Score (PS) on match player cards

#### Improvements & Bug Fixes
- Adds better error messaging on search screen when no player is found
- Adds pull to refresh on search screen and on player details screen
- Fixes some crashes around when new heroes are added to the API
- Fixes some crashes when authenticating with Discord the first time you use Chrome


## 0.1.0

- Add "X" button to search field to clear the field
- Add circular shape to user's discord avatar in on the profile screen
- Added new error handling messages and states for network related issues
