# POO.TV

Assignment for 2nd year OOP module.

Netflix rip-off with different functionalities and a currency system.

### The Application
POO.TV (or OOP.TV as intellectuals often refer to it) is a small scale application
for simulating the well-known processes found behind everyday life with Netflix/HBO/YouTube
searches. It includes 'pages', more exactly menus which help navigate through
the application. It operates with a label-based system, as every command has
its very own labels which help describe it. Among these labels one may find 'features',
which refer to operations a page might be able to do.

The application also handles a number of possible errors, edge cases, things that
a client might be prone to do out of indifference towards the system, a mistake or
purely because they wanted to mess around. As POO.TV only stands in the
first stage of development, the amount of errors handled is small and the code
may be vulnerable.

Attached to the base code is a database, which stores information about the
registered users, the available movies, particularities about them, which menu/page
is currently active and displayed and so on.

Among the functionalities of this application, the notable ones are:
* Filtering and sorting movie lists;
* Upgrading from a standard account to a premium one;
* Access restrictions;
* Back button;
* Complex features;
* Subscriptions & notifications.

## Implementation - First Stage
The concept may be simple, but in reality a lot of stuff must be takes into consideration
when building such a system. This is why the ```Facade``` Design Pattern has been used
as a base skeleton. ```PageKeeper.java``` is the facade, the 'keeper' of the content, by which
is meant the menus. A client, be it the programmer or a regular individual, comes face to face
with an instance of said facade and orders whatever is necessary. PageKeeper is basically
the powerhouse of the program, as it hosts a whole factory-like construction.

That being said, the main object offered by PageKeeper is, of course, a ```Page```.
Page has been declared abstract and has become the parent of many other menus, which
are : ```Login, Register, Un/Authenticated, Movies, See Details, Upgrades, Logout.```

Page has an interesting system of legalizing features and page manipulations. There
are two lists, called ```legalFeatures``` and ```legalPageSwitches```. legalFeatures
stores those features characteristic to _one_ page. legalPageSwitches stores those
pages the client can _jump unto_ from the current one. Using ```abstractization``` and
```inheritance```, those two become unique for every different page. This is the
method used in the program to keep track of what a client can and cannot do while
operating on a page. There are other helper functions, which help manage the activity
of the pages, such as ```isActive``` and ```activeSwitch```.

## Implementation - Second Stage
Compared to the first stage, 4 design patterns have been used: ```Facade```, ```Memento```,
```Builder``` and ```Command```. Memento has been used for the back button, as to remember each
state. A state stores one page name. Having this, a queue of mementos can be formed.
For the .json parsing functionalities, Builder was the choice. Each object is sequentially
formed, leaving space to play around with different outputs. For the actual commands,
I used Command, separating it in 4 branches: 'ChangePage', 'OnPage', 'Back' and Database.
Facade works as a complex system, containing Command as its underling to operate with.
Every command is processed and executed separately.

### Structures
The structures used in this project are the following:
* ```Input``` - holds the whole bulk of information transferred from .json;
* ```User``` - simple structure keeping the data on the user;
* ```Movie```- simple structure keeping the data on the movie;
* ```Credentials``` - related to user, stores the sensitive information;
* ```Filters``` - contains the filtering flags;
* ```Sort``` - contains the sorting flags, used in filtering;
* ```Contains``` - flags showing which labels should be parsed in the filtering process;
* ```Action``` - your best friend, it manages all of the above in one command;

### Database
The database manages registered users and movies. It keeps track of whatever
the client has in front of them so they can operate accordingly.

It also updates the logged in user and remembers them. This class also hosts
multiple useful functions used during the program to manage data.

### Printing to Output
Because the original skeleton for this project did not include a proper inputting
system such as the first homework, I took the liberty to copy some lines of code
from GwentStone[1] and use them here.

As I have done in GwentStone, I created a ```Printer``` class to play around
with the output ArrayNode and create proper .json structures.

For the second stage of the project, Printer became more complex in structure.

#### Problems
The checker was so rudimentary that I questioned myself a lot while writing code.
At the same time, many instructions given on the ocw[2][3] were either lacking, or
completely absent, and some instructions were wrong, as they did not match the
requirements of the reference tests. This be why I followed more the .jsons than
I have actually read the homework 'story'.

#### References
[1] - https://github.com/Cynerdus/GwentStone <br>
[2] - https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa1 <br>
[3] - https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/proiect/etapa2

##### Copyright 2023 Popescu Cleopatra 323CA