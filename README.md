
# FixErrorHelper 

FixErrorHelper is a plugin specifically developed for Eclipse with the aim of simplifying the understanding of console messages related to Java errors or exceptions. The plugin should clearly and concisely provide the cause of the problem, the specific error, and possible solutions. Based on the established return pattern, FixErrorHelper aims to help developers identify and resolve issues more efficiently.

    [ add java error/exception screenshot in console ]
    
    [ add plugin interface screenshot ]


# Eclipse Marketplace

    [ add link ]


# Use

Before using the plugin, you need to configure it by setting your OpenAI API key. To configure the plugin, follow these steps:

- Access the plugin preferences page at: Preferences > FixErrorHelper
- In the API Key field, enter your OpenAI API key (you can create a free account at https://platform.openai.com/signup and get $5 free credits).
- In the Language field, select the return language for plugin responses.
- Click Apply to save the changes.


After configuring the plugin, follow these steps to use it:

- Run your Java application in the Eclipse IDE.
- If an error or exception is thrown, look for the message in the console.
- Click on the plugin icon or access via shortcut (command + 6).
- A pop-up window will appear with information regarding the error/exception and possible solutions to resolve the problem.

Note: For the plugin to work, you need to have an internet connection to access the OpenAI API.


# Technologies Used

- Java 21.0.3 (LTS)
- Eclipse Plugin Development Environment (PDE)
- Regex
- Git
- GitHub


# Cloning the Repository

    git clone https://github.com/larissadesp/fixerrorhelper.git


# Setting Up the Environment

Open Eclipse and import the project as a plugin project:
- File > Import > Existing Projects into Workspace > Select root directory > Browse > Select the directory of the cloned project.


# Build and Run

1. To compile the plugin:
- Right-click on the project > Run As > Eclipse Application.

2. Eclipse will restart with the FixErrorHelper plugin activated.
  
  [ add plugin screenshot to toolbar and shortcut command + 6 ]


# How to Contribute:

- Fork the repository.
- Create a branch for your feature (git checkout -b feature/new-feature).
- Commit your changes (git commit -m 'Add new feature').
- Push to the branch (git push origin feature/new-feature).
- Open a Pull Request


# License 

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

