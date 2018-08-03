# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Fixed
- Scaffold Creation: Validation on special characters

## [2.6.0] - 2018-08-02
### Added
- Payment widget. Now you can pay a scaffold using a widget.
- Widget: an iFrame element for embedding the payment widget to the seller's website
- Widget: Browser detecting logic
- Widget: Error tips
- Widget: Web3 script with required functionality

## [2.5.0] - 2018-07-27
### Added
- Scaffold v2
- Scaffold activation flag
- Activate and deactivate a scaffold from the scaffold page
- Handling of different scaffold versions by API
- Readable exception messages
- Payment widget authorization
- User message when user's token is expiring during an active session

### Changed
- Time standard is changed to UTC
- Only integer numbers allowed as share value
- System messages moved to a separate file

## [2.4.0] - 2018-07-13
### Added
- Scaffold events on a scaffold page
- Events real-time update
- Current token expiration message
- Edit a scaffold title
- Automatic reconnection to a chain if a connection is lost

### Changed
- Smart contract code optimization

### Removed
- Description field from a scaffold

## [2.3.0] - 2018-06-29
### Added
- Edit scaffold description
- Developers keys management:
- Generate API key
- Show API keys list
- Add API keys name
- Edit API keys name
- Deactivate API key
- Communication protocol between API and scaffold
- Scaffold events decoder
- Backend: events are sent by a webhook
- Scaffold: unit tests
- Application unit tests

### Changed
- Default values are no longer required for additional scaffold properties
- Application will automatically reconnect to a chain of a connection is lost

## [2.2.1] - 2018-06-25
### Added
- Webhook URL validation
- Currency type is required for a template
- Reaching Scaffold properties limit message
- Client validation for Scaffold properties duplicates

### Fixed
- Scaffold Compilation: 400 bad request in case of sending "properties.type": "BOOLEAN"
- Scaffold Shareholders: 500 NPE in case of adding/editing shareholders by a backend
- Scaffold Creation: 400 bad request in case of providing more than 2 scaffold properties

## [2.2.0] - 2018-06-22
### Added
- Scaffold templates. Now you can fill in scaffold fields and add additional scaffold properties in one click using predefined templates
- Scaffold templates samples
- Transaction split. Now you can add several transaction recipients and set recipients shares after scaffold deployment
- Transaction split: add an additional recipient
- Transaction split: set additional recipient's share
- Transaction split: edit additional recipient's share
- Transaction split: delete an additional recipient

### Changed
- Unit tests

## [2.1.0] - 2018-06-18
### Added
- Public API Specification
- Scaffold deployment via MetaMask
- Scaffold activation via MetaMask
- The number of inactive Scaffolds is limited to 10 Scaffolds
- Choose Scaffold deployment method
- Active Ethereum network recognition
- Off-chain data synchronization
- Receive Scaffolds
- Receive Scaffold transactions
- Set Scaffold webhooks
- Receive Scaffold state
- Backend: OPEN API Keys management
- Backend: Update Scaffold Information
- Backend: Scaffold templates
- Backend: Split Transaction
- Scaffold: Split Transaction

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
[2.6.0]: https://github.com/OpenFuturePlatform/open-api/compare/v2.5.0...v2.6.0
[2.5.0]: https://github.com/OpenFuturePlatform/open-api/compare/v2.4.0...v2.5.0
[2.4.0]: https://github.com/OpenFuturePlatform/open-api/compare/v2.3.0...v2.4.0
[2.3.0]: https://github.com/OpenFuturePlatform/open-api/compare/v2.2.1...v2.3.0
[2.2.1]: https://github.com/OpenFuturePlatform/open-api/compare/v2.2.0...v2.2.1
[2.2.0]: https://github.com/OpenFuturePlatform/open-api/compare/v2.1.0...v2.2.0
[2.1.0]: https://github.com/OpenFuturePlatform/open-api/compare/v2.0.0...v2.1.0
[2.0.0]: https://github.com/OpenFuturePlatform/open-api/compare/8ea69084ef657f66976518827873c9c922970ce6...v2.0.0