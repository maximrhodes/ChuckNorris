# Chuck Norris App

> The App was made in the context of the **[TeachingAndroid](https://github.com/NicolasDuponchel/TeachingAndroid)** Course OUAP-4316. 

The goal of the app is to be able to display Chuck Norris jokes taken from the **[Chuck Norris API](https://api.chucknorris.io/)**.
It should have the ability to :
- **save** jokes as favorites
- **share** jokes to your friends 
- keep the same jokes regardless of how the phone is **oriented**
## Creation of the App

I have followed the different steps in order to create the app :

**1. Create a dynamic list and a layout**

 > I used the help of a custom View (**[RecyclerView](https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView)**) and an adapter.

**2. Get the jokes from the web API**

> I create a data model in **json** that matches the one of the jokes returned by the API. Then i used **Retrofix** and **RxJava2** to fetch the jokes from the API and put them on the app.

**3. Display the joke on the App**

> I used a **loader** and an **Observable** in order to be able to display the downloaded joke. We ensure that we can load more jokes if we scroll on the app.

**4. Customize the UI**

> I saved the **state** of the instance in order to keep it if the screen is rotated. Then i implemented the abitily to share and save jokes thanks to **Intent**.

## MVVM pattern 



