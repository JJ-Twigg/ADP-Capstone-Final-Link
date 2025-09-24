## Project: 	Hotel-booking System
- Started: 	12.03.25
- Updated: 	16.09.25
- Status: 	work-in-progress
- Branch:	master-dev1
---

### Software-class Diagram
![hms_erd.drawio.png](hms_erd.drawio.png)
=======

### About project
- A Java project for managing a hotel-reservation system, created using the IntelliJ IDE 


### Download
```
git clone https://github.com/ammaar0x01/ADP3_capstone_project.git
OR
git clone --branch [branch-name] https://github.com/ammaar0x01/ADP3_capstone_project.git
```


### Execute
- Load and run the project using the IntelliJ IDE
---

zaids dashboard
first thing had to change /view in his controller to /scenes
so files were picked up.

Then to get css working called stylesheets and applied 
when switching page

then had to change resolution to 1100 x 600


-----


-------
updating UI's steps
rename to eg reservationFinal.fxml
change controller to what the old ugly ui is using.
now u need to change method calls inside the new fxml, to 
whats in the controller u just added. so put old ui and new ui fxml side by side
and change new uis on action to old uis on action for thte 3 buttons add del update

then u need to change fx:id of table columns in new ui fxml, to match
the controllers fxid for table columns. ALso u need to match
controller table name fxid fx:id="reservationTable" for tableview,
so not just columns

also for every file load from now on needs
loader.setControllerFactory(MainFinal.getSpringContext()::getBean);
GETTING SPRINGBOOT FROM MAINFINAL.  ITS THE ONLY ENTRY TO PROGRAM

---
style="-fx-background-color: white;" causes right graphical glitch
why

easy fix, i removed extra cols, so tableview witdth needs to
be reduced to reemove ugfly ui grapgical glitch or u need columns
to take taht space. either or,


1 need to finish combobox booking type
then if they chose room, only then run event pane.

2 also once add event is cliekd once, button add
must be made unusable, to prevent duplciate fk