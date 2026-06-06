# Hospital Patient Queue Manager (Java GUI & SQLite) 🏥🖥️

A robust, graphical desktop application built with Java Swing that manages hospital patient queues. It integrates with a serverless SQLite database to ensure persistent storage of patient records, completely eliminating the need for complex database installations.

## 🚀 Features
* **Graphical User Interface:** Clean, intuitive desktop window built with Java Swing.
* **Zero-Setup Database:** Utilizes SQLite to automatically generate and manage a local database file (`hospital.db`).
* **Real-time Data:** View all waiting patients in a dynamic `JTable` that pulls live from the database.
* **Auto-generated IDs:** Secure, sequential ID generation handled directly by the SQLite engine.

## 🛠️ Technologies & Tools
* **Frontend:** Java (Swing, AWT)
* **Backend:** Java (JDK 8+)
* **Database:** SQLite
* **Connectivity:** Java Database Connectivity (JDBC) / `sqlite-jdbc.jar`

## ⚙️ How to Run
1. Clone this repository.
2. Ensure you have the SQLite JDBC driver (`sqlite-jdbc.jar`) in your project's build path/library.
3. Compile and run `HospitalGUI.java`.
4. The application will automatically create the `hospital.db` database file the first time it boots up!
