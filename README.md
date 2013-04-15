Last updated: April 15, 2013

MetroBike - some description stuff here...

================================================================================
Usernames
================================================================================
coreyh3
dutchscout

================================================================================
Contents
================================================================================

1. Git / GitHub instructions
    1.1 Getting set up 
    1.2 Pushing and pulling from the repo


================================================================================
1. Git / GitHub instructions
================================================================================

1.1 Getting set up
--------------------------------------------------------------------------------

Step 1: Create an account at GitHub.com

Step 2: Follow this guide to install and set up git on your machine:
https://help.github.com/articles/set-up-git

Step 3: Get added as a collaborator
If you have not already ensure you have been added as a collaborator to the repo
for our project. Email Adrian with your GitHub username if you have not been 
added.

Step 4: Create an ssh key
You need to set up a way to 'talk' to the remote repository. You can do this 
using an ssh public key or HTTP authentication. It is best to use ssh.
Follow this guide to set up an ssh key:
https://help.github.com/articles/generating-ssh-keys

Step 5: clone the repo to your local machine
Use the following to command to create a local copy of the repo on your machine
which is where you will make your changes:

git clone git@github.com:alaurenz/metrobike.git


1.2 Pushing and pulling from the repo
--------------------------------------------------------------------------------

To pull changes that others have made to the 'master' repository on GitHub you
must be within the directory of your local repo (the directory created
when you ran 'git clone'). Within the directory run:

git pull origin master

To push changes to the master, after you have commited them run:

git push origin master





