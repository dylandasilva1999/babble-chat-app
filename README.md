<!-- PROJECT LOGO -->
<br />

![GitHub repo size](https://img.shields.io/github/repo-size/dylandasilva1999/babble-chat-app?color=%232f296a)
![GitHub watchers](https://img.shields.io/github/watchers/dylandasilva1999/babble-chat-app?color=%232f296a)
![GitHub language count](https://img.shields.io/github/languages/count/dylandasilva1999/babble-chat-app?color=%232f296a)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/dylandasilva1999/babble-chat-app?color=%232f296a)
[![LinkedIn][linkedin-shield]][linkedin-url]

<h6 align="center">Dylan da Silva Android 302</h6>
<p align="center">
</br>
   
  <a href="https://github.com/dylandasilva1999/babble-chat-app">
    <img src="Images/App Icon.png" alt="Logo" width="140" height="140">
  </a>
  
  <h3 align="center">Babble</h3>

  <p align="center">
    Bigger. Better. Babble <br>
      <a href="https://github.com/dylandasilva1999/babble-chat-app"><strong>Explore the docs »</strong></a>
   <br />
   <br />
   <a href="https://youtu.be/P1HdUO6RXXc">View Demo</a>
    ·
    <a href="https://github.com/dylandasilva1999/babble-chat-app/issues">Report Bug</a>
    ·
    <a href="https://github.com/dylandasilva1999/babble-chat-app/issues">Request Feature</a>
  </p>

<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Project](#about-the-project)
* * [Project Description](#project-description)
  * [Built with](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [How to install](#how-to-install)
* [Features and Functionality](#features-and-functionality)
   * [Features](#features)
   * [Functionality](#functionality)
* [Concept Process](#concept-process)
   * [Ideation](#ideation)
   * [Wireframes](#wireframes)
   * [User-flow](#user-flow)
* [Development Process](#development-process)
   * [Implementation](#implementation)
   * [Peer Reviews](#peer-reviews)
* [Mockups](#mockups)
* [Video Demonstration](#video-demonstration)
* [Roadmap](#roadmap)
* [Contributing](#contributing)
* [License](#license)
* [Contact](#contact)
* [Acknowledgements](#acknowledgements)

<!--PROJECT DESCRIPTION-->
## About the Project

![image1][image1]

### Project Description

According to Merriam Webster, the definition of Babble, is to "talk enthusiastically or excessively" (2021). Which relates to the theme of the project of creating a closed-group chat application. As per Wardle and Dotto, statistics state/show that 30% of Whatsapp users make use of private group chats "set up with workmates", meaning close-groups for chatting is used quite often (2020). Therefore having an application where users can do individual babbling and in groups at ease, is essential.

Babble is a project for a Term 2 module Android Development Expanded, where we were tasked to develop an Android closed-group chat application built with Kotlin for the application logic and Google Firebase to manage the authentication and backend. The theme of the project is based of Whatsapp that sparked a public outcry due to an update to their terms and privacy policy, and users are desperately searching for alternative platforms. Therefore as a developer, I saw this as a golden opportunity to build my own messaging platform, in this case, Babble.

Babble is closed-group chat application! Babble is built for for those enthusiastic and excessive chatting conversations between fellow babblers.

### Built With

* [Android Studio](https://developer.android.com/studio)
* [Kotlin](https://kotlinlang.org/)
* [Firebase](https://firebase.google.com/?gclid=Cj0KCQjwse-DBhC7ARIsAI8YcWItyGR2ha-LRzTOOglVa0RTNXSePdd7fMLvbCnR6ERBs4oI4ken-SYaAmsqEALw_wcB&gclsrc=aw.ds)
* [GitHub](https://github.com/)

<img src="https://upload.wikimedia.org/wikipedia/commons/e/e3/Android_Studio_Icon_%282014-2019%29.svg" width="4%" height="4%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://upload.wikimedia.org/wikipedia/commons/0/06/Kotlin_Icon.svg" width="4%" height="4%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://upload.wikimedia.org/wikipedia/commons/9/91/Octicons-mark-github.svg" width="5%" height="5%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://upload.wikimedia.org/wikipedia/commons/4/46/Touchicon-180.png" width="5%" height="5%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

<!-- GETTING STARTED -->
## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Ensure that you have the latest version of Android Studio installed on your machine. The Kotlin plugin will also be required.

### How to install

### Installation
Here are a couple of ways to clone this repo:

1. Android Studio </br>
`Android Studio` -> `File` -> `New` -> `From Version Control` -> `Git`</br>
Enter `https://github.com/dylandasilva1999/babble-chat-app.git` into the URL field and press the `Clone` button.

2. Command-line + Android Studio </br>
Run the following in the command-line to clone the project:
   ```sh
   git clone https://github.com/dylandasilva1999/babble-chat-app.git
   ```
Open `Android Studio` and select `File | Open...` from the menu. Select cloned directory and press `Open` button

<!-- FEATURES AND FUNCTIONALITY-->
## Features and Functionality

### Features

![image2][image2]
### Secure Authentication

Secure log in and sign up.

![image3][image3]
### Image Messages

Babble and send image messages to other babblers.

### Text Messages

Babble and send text messages to other babblers.

![image4][image4]
### Profile View

Update your profile information.

### Sign In Notification

Recieve push notifications when signing in.

### Functionality

* `Linear`, `Constraint`, `Recycler View`, and `Frame` layouts were used to create the layouts.
* Made use of both `Fragments` and `Activities` for navigation.
* `Firestore Database` for storing users and messages.
* `Firebase Authentication` for users.
* `Firebase Storage` for images sent and profile images
* `Glidemodule` for image messages and messages.
* `Retrofit` for push notifications.
* `Intents` are used transfer certain information from one activity to another.
* `Lottie Animations` is for displaying success animations on sign in and sign up.

<!-- CONCEPT PROCESS -->
## Concept Process

The `Conceptual Process` is the set of actions, activities and research that was done when starting this project.

### Ideation

![image5][image5]
<br>
![image6][image6]

### Wireframes

![image7][image7]

### User-flow

![image8][image8]

<!-- DEVELOPMENT PROCESS -->
## Development Process

The `Development Process` is the technical implementations and functionality done in the backend of the application.

### Implementation

#### Challenges

* The ability to send messages to specific users.
* Profile image updating and user info update.

### Peer Reviews

The `Peer Reviews` were conducted by my fellow students and lecturer.

#### Feedback

* A major highlight was fixing on bad bug I had.
* Another highlight is the sending of images, and the success animations.

#### Future Changes

* Add group chat functionality in the chats section.
* Sending of emoji and gifs in any chat.

<!-- MOCKUPS -->
## Mockups

![image9][image9]
<br>
![image10][image10]

<!-- VIDEO DEMONSTRATION -->
## Video Demonstration

To see a run through of the application, click below:

[View Demo](https://youtu.be/P1HdUO6RXXc)

<!-- PROMO VIDEO -->
## Promo Video

To see the promo video, click below:

[View Promo Video](https://youtu.be/pnr8wTz3K8A)

<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/dylandasilva1999/babble-chat-app/issues) for a list of proposed features (and known issues).

<!-- CONTRIBUTING -->
## Contributing

Contributions are what makes the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<!-- AUTHORS -->
## Authors

* **Dylan da Silva** - [DylandaSilva](https://github.com/dylandasilva1999)

<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.\

<!-- LICENSE -->
## Contact

* **Dylan da Silva** - [@dylandasilva.designs](https://www.instagram.com/dylandasilva.designs/) - 190082@virtualwindow.co.za
* **Project Link** - https://github.com/dylandasilva1999/babble-chat-app

<!-- ACKNOWLEDGEMENTS -->
## Acknowledgements

* [My lecturer Armand Pretorius](https://github.com/ArmandPretorius)
* [Whatsapp Statistics Article](https://firstdraftnews.org/long-form-article/closed-groups-messaging-apps-and-online-ads/)
* [Babble Definition](https://www.merriam-webster.com/dictionary/babble)
* [Lottie Animations](https://lottiefiles.com/)
* [Firebase](https://firebase.google.com/)
* [Youtube Tutorial](https://www.youtube.com/watch?v=uB7WeED1d1w&list=PLB6lc7nQ1n4h5tzT3tu_YSy9VNrVUR_4W)
* [Flaticon](https://www.flaticon.com/)
* [Illustrations](https://www.drawkit.io/product/drawkit-classic)

<!-- MARKDOWN LINKS & IMAGES -->
[image1]: Images/Image1.png
[image2]: Images/Image2.png
[image3]: Images/Image3.png
[image4]: Images/Image4.png
[image5]: Images/Image5.png
[image6]: Images/Image6.png
[image7]: Images/Image7.png
[image8]: Images/Image8.png
[image9]: Images/Image9.jpg
[image10]: Images/Image10.jpg

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=flat-square&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/dylan-da-silva-72b56a20a/
