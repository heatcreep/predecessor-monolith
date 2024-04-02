# Release Notes

## 1.3.2 - Latest

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
