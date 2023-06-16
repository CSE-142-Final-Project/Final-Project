# Testing Report
## What we tested
For this project we mostly just tested the underlying networking code. While we did test the final product it was not tested nearly as rigourously as it was a significanly simpler than the underlying networking code
## How we tested
### Unit tests
* We wrote unit tests that covered basically all of the networking back-end
* The  unit tests do have the limitation of not testing cross device interactions as the servers and clients are all run on the same device
### Manual testing
* We used manual tests to test all of the 
* Both of us launched a server and tested conect clients to it over the school network
* We have also tested with many clients conected to one central server with all of them running on one device
## Dealing with bugs found by other testers
* We didn't have any other testers :(
## Unresolved bugs
* For some bizare reason, sometimes packets are not successfully delivered from the server to a connecting client running on the same device
	* We can't reliably reproduce this though
## Future testing
- [ ] Get full unit test coverage off the networking code
- [ ] Do more manual testeing of the final project
- [ ] Explore ways to unit test the actual final project
