# Release Notes

## 0.6.6 - Latest

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