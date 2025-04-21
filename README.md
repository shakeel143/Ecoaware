# 🌱 EcoGuardians: Sustainability Starts with Simplicity 🌍

**EcoGuardians** is an Android application designed to educate users about environmental issues and promote sustainable living practices. The app focuses on providing accurate, engaging content, interactive features, and community engagement tools to inspire positive change.

---

## 📁 Table of Contents
1. [📘 Overview](#overview)
2. [✨ Features](#features)
3. [🖼️ Screenshots](#screenshots)
4. [📱 App Description](#app-description)
5. [🧰 Tech Stack](#tech-stack)
6. [🚀 Getting Started](#getting-started)
7. [🤝 Contributing](#contributing)
8. [📄 License](#license)

---

## 📘 Overview

EcoGuardians empowers individuals to make environmentally conscious decisions by offering educational resources, actionable tips, a sustainability tracker, a green community forum, and multilingual support. The app is built with accessibility in mind, ensuring it is usable by everyone.

---

## ✨ Features

- 📚 **Environmental Education**: Learn about climate change, pollution, biodiversity, and sustainability.
- 🏆 **Eco-Friendly Tips & Challenges**: Daily/weekly tasks to encourage green habits.
- 📊 **Sustainability Tracker**: Log actions like recycling and public transport use.
- 💬 **Green Community Forum**: Share posts, comments, and ideas.
- 📖 **Resource Library**: Access articles, guides, and helpful links.
- 🌐 **Multilingual Support**: Supports various global languages.
- ♿ **Accessibility**: High-contrast UI, screen readers, and scalable text.

---

## 🖼️ Screenshots

> _Placeholder images below (replace with actual app screenshots)_

| Screen | Screenshot |
|--------|------------|
| 🌊 Splash | ![Splash Screen](screenshots/splash.png) |
| 🔐 Logic | ![Logic Screen](screenshots/logic.png) |
| 📝 Signup | ![Signup Screen](screenshots/signup.png) |
| 🏠 Home (Dashboard) | ![Home Screen](screenshots/home.png) |
| 📗 Environmental Education | ![Education Screen](screenshots/education.png) |
| 🧠 Tips and Challenges | ![Tips Screen](screenshots/tips.png) |
| 📈 Tracker | ![Tracker Screen](screenshots/tracker.png) |
| 👥 Community | ![Community Screen](screenshots/community.png) |
| ➕ Add Post (Create Post) | ![Add Post Screen](screenshots/add_post.png) |
| 📚 Resources | ![Resources Screen](screenshots/resources.png) |
| ⚙️ Settings (Change Language) | ![Settings Screen](screenshots/settings.png) |
| 🌍 Multilanguage Support | ![Multilanguage Screen](screenshots/multilanguage.png) |

---![IMG-20250421-WA0008](https://github.com/user-attachments/assets/40b75135-e201-434c-8f22-2cba01d0046b)![IMG-20250421-WA0015](https://github.com/user-attachments/assets/6c08b92b-7277-4056-badf-22a42c02fa78)
![IMG-20250421-WA0014](https://github.com/user-attachments/assets/c00c1eae-4b6d-4cf9-950e-d7dfd1b4852f)
![IMG-20250421-WA0013](https://github.com/user-attachments/assets/9edd99cd-e118-40c0-8e96-0be25ee832b4)
![IMG-20250421-WA0012](https://github.com/user-attachments/assets/e597c47f-a6cb-4c07-ab75-ff23ec015d84)
![IMG-20250421-WA0011](https://github.com/user-attachments/assets/db75b1b1-aa9b-467c-9847-cdec78991498)
![IMG-20250421-WA0010](https://github.com/user-attachments/assets/afbd2b5d-a8b4-4f51-af1d-515bce4b7100)
![IMG-20250421-WA0009](https://github.com/user-attachments/assets/8da28e6d-a85d-48ec-9abf-8243086d0864)

![IMG-20250421-WA0007](https://github.com/user-attachments/assets/883024e0-1cd4-4c17-9311-e58f9ffa9e80)
![IMG-20250421-WA0006](https://github.com/user-attachments/assets/ceade183-d926-48f8-92c9-2bfc9088202f)
![IMG-20250421-WA0017](https://github.com/user-attachments/assets/7073f392-e3be-46fa-af80-537f33d27f34)
![IMG-20250421-WA0016](https://github.com/user-attachments/assets/d7c8eb00-7fea-4fc1-875e-0f2f8df334d6)


## 📱 App Description

### 🔐 Authentication
- Uses Firebase Authentication for email/password login.

### 🧱 Firebase Realtime Database Structure:
```json
{
  "Comments": {
    "<postId>": {
      "<commentId>": {
        "commentId": "<commentId>",
        "text": "Comment Text",
        "timestamp": <timestamp>,
        "userId": "<userId>",
        "userName": "<userName>"
      }
    }
  },
  "CommunityPosts": {
    "<postId>": {
      "commentsCount": 1,
      "likes": {
        "<userId>": true
      },
      "likesCount": 1,
      "message": "Post message",
      "postId": "<postId>",
      "timestamp": <timestamp>,
      "userId": "<userId>"
    }
  },
  "SustainabilityLogs": {
    "<userId>": {
      "<logId>": {
        "action": "Recycled",
        "timestamp": <timestamp>
      }
    }
  },
  "Users": {
    "<userId>": {
      "email": "user@example.com",
      "language": "English",
      "name": "User Name",
      "uid": "<userId>"
    }
  }
}
```

---

## 🧰 Tech Stack

- **🛠️ Language:** Java
- **💻 IDE:** Android Studio
- **🔥 Backend:** Firebase Authentication & Firebase Realtime Database
- **🛂 Libraries Used:**
  - 📊 MPAndroidChart
  - 🔥 Firebase (Auth + RTDB)
  - 🖼 Glide
  - 🎨 Material Components

---

## 🚀 Getting Started

1. **Clone the Repository**
```bash
git clone https://github.com/your-username/EcoGuardians.git
```

2. **Navigate to the Project**
```bash
cd EcoGuardians
```

3. **Build the Project**
```bash
./gradlew build
```

4. **Setup Firebase**
   - Add `google-services.json` to `app/` directory.
   - Enable Authentication & Database in Firebase Console.

5. **Run the App**
   - Open in Android Studio.
   - Connect device or use emulator.
   - Press **Run** ▶️

---

## 🤝 Contributing

We love community contributions! 💚

1. Fork the repository 🍝  
2. Create a feature branch (`git checkout -b feature-name`) 🌿  
3. Commit your changes 📝  
4. Push to your fork (`git push origin feature-name`) 🚀  
5. Submit a Pull Request 🔁

---

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for more details.

---

## 📬 Contact

📧 Email: [ecoguardians@example.com](mailto:ecoguardians@example.com)  
🌐 Website: [www.ecoguardians.com](https://www.ecoguardians.com)

---

> 💡 Thank you for supporting sustainability with **EcoGuardians**! 🌳  
> _“Small steps lead to big changes.”_
