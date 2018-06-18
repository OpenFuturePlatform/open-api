# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]
- Dropdown for deployment method
- Active Ethereum network recognition
- Stateless authentication
- Setting Webhooks

## [2.0.0] - 2018-06-04
### Added
- Scaffold activation: transfer at least 10 OPEN Tokens to a scaffold to make it active.
- Active scaffold flag
- Scaffold activating message
- Scaffold list pagination
- Immediate funds withdrawal. Now contract amount is directly transferred to developers wallet without additional
  actions required.
- Logo - link to /scaffolds
- Link to Etherscan
- Scaffold templates functionality
- Keys management

### Changed
- API code refactored from Node.js to Kotlin
- Google authentication
- Log in
- Log out
- Get current user
- Save user after authentication
- Scaffold processing
- Scaffold template processing
- Scaffold compiling
- Scaffold deploy
- Scaffold display
- Get scaffolds of current user
- Get scaffold by his address
- API web interface is refactored
- Front-end build into target back-end directory
- Routing and link refactoring
- Supply integration with new API
- Default currency and auto convert into Ethereum
- Creating scaffold with key select input

### Removed
- "Withdraw" button is removed

[Unreleased]: https://github.com/OpenFuturePlatform/open-api/compare/master...sprint
[2.0.0]: https://github.com/OpenFuturePlatform/open-api/compare/8ea69084ef657f66976518827873c9c922970ce6...v2.0.0