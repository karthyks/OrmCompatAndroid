# OrmCompatAndroid

<h3>Usage</h3>

Add ormcompat to your dependencies.
```
compile 'com.github.karthyks:ormcompat:1.0.0'
```

Lets consider the following SampleData is the class to be saved in SQlite.

```
public class SampleData {
    private String name;
    private String address;
    private String mobile;
}
```

<h4>Step 1</h4>
Extend the class with DataModel class and implement the methods as shown in the sample app
<h4>Step 2</h4>
Now create your ContentHelper class to build your columns required for the table by extending
StoreContentHelper class.
<h4>Step 3</h4>
Create a data loader class and a inner Dao class  to perform crud operations.
<h4>Step 4</h4>
Implement all the static variable in the application class.<br/>
<b>Note :</b> StoreContentHelper.AUTHORITY should be your parent package name.

<br/><br/><br/>
Finally initialize your data loader class and call the loadrequest method.

The following is the callback from the database crud operations.
```
@Override
  public void onDataAvailable(List<SampleData> dataModels) {
    Log.d(TAG, "onDataAvailable: " + dataModels.size());    
  }
```

