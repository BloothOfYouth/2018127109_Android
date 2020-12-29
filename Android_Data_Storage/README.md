# 数据存储全方案

​		任何一个应用程序其实说白了就是在不停地和数据打交道，我们聊 QQ、看新闻、刷微博所关心的都是里面的数据，没有数据的应用程序就变成了一个空壳子，对用户来说没有任何实际用途。那么这些数据都是从哪来的呢？现在多数的数据基本都是由用户产生的了，比如你发微博、评论新闻，其实都是在产生数据。

 		**瞬时数据**，就是指那些存储在内存当中，有可能会因为程序关闭或其他原因导致内存被回收而丢失的数据。这对于一些关键性的数据信息来说是绝对不能容忍的，谁都不希望自己刚发出去的一条微博，刷新一下就没了吧。那么怎样才能保证让一些关键性的数据不会丢失呢？这就需要用到数据持久化技术了。



## 持久化技术简介

​		数据持久化就是指将那些内存中的瞬时数据保存到存储设备中，保证即使在手机或电脑关机的情况下，这些数据仍然不会丢失。**保存在内存中的数据是处于瞬时状态的**，**而保存在存储设备中的数据是处于持久状态的**，持久化技术则是提供了一种机制可以让数据在瞬时状态和持久状态之间进行转换。

​		持久化技术被广泛应用于各种程序设计的领域当中，而我们要探讨的自然是 [Android](http://lib.csdn.net/base/android) 中的数据持久化技术。Android 系统中主要提供了三种方式用于简单地实现数据持久化功能，即文件存储、SharedPreferences 存储以及[数据库](http://lib.csdn.net/base/mysql)存储。当然，除了这三种方式之外，你还可以将数据保存在手机的 SD 卡中，不过使用文件、SharedPreference 或数据库来保存数据会相对更简单一些，而且比起将数据保存在 SD 卡中会更加的安全。



------



## 文件存储

​		文件存储是 Android 中最基本的一种数据存储方式，它不对存储的内容进行任何的格式化处理，所有数据都是原封不动地保存到文件当中的，因而它比较适合用于存储一些简单的文本数据或二进制数据。**如果你想使用文件存储的方式来保存一些较为复杂的文本数据，就需要定义一套自己的格式规范，这样方便于之后将数据从文件中重新解析出来**。

​		那么首先我们就来看一看，Android 中时如何通过文件来保存数据的。

### 将数据存储到文件中

​		Context 类中提供了一个 openFileOutput() 方法，可以用于将数据存储到指定的文件中。这个方法接收两个参数，第一个参数是文件名，在文件创建的时候使用的就是这个名称，注意这里指定的文件名不可以包含路径，因为所有的文件都是默认存储到 `/data/data/<package name>/files/`目录下的。第二个参数是文件的操作模式，主要有两种模式可选，**MODE_PRIVATE** 和**MODE_APPEND**。其中 **MODE_PRIVATE 是默认的操作模式，表示当指定同样文件名的时候，所写入的内容将会覆盖原文件中的内容，而 MODE_APPEND 则表示如果该文件已存在就往文件里面追加内容，不存在就创建新文件**。其实文件的操作模式本来还有另外两种，MODE_WORLD_READABLE 和 MODE_WORLD_WRITEABLE，这两种模式表示允许其他的应用程序对我们程序的文件进行读写操作，不过由于这两种模式过于危险，很容易引起应用的安全性漏洞，现已在 Android 4.2 版本中被废弃。

​		openFileOutput() 方法返回的是一个 FileOutputStream 对象，得到了这个对象之后就可以使用 [Java](http://lib.csdn.net/base/javaee) 流的方式将数据写入到文件中了。以下是一段简单的代码示例，展示了如何将一段文本内容保存到文件中：

```java
public void save() {  
  String data = "Data to save";  
  FileOutputStream out = null;  
  BufferedWriter writer = null;  
  try {  
    out = openFileOutput("data", Context.MODE_PRIVATE);  
    writer = new BufferedWriter(new OutputStreamWriter(out));  
    writer.write(data);  
  } catch (IOException e) {  
    e.printStackTrace();  
  } finally {  
    try {  
      if (writer != null) {  
        writer.close();  
      }  
    } catch (IOException e) {  
      e.printStackTrace();  
    }  
  }  
} 
```

 		如果你已经比较熟悉 Java 流了，理解上面的代码一定轻而易举吧。这里通过 openFileOutput() 方法能够得到一个 FileOutputStream 对象，然后再借助它构建出一个 OutputStreamWriter 对象，接着再使用 OutputStreamWriter 构建出一个 BufferedWriter 对象，这样就可以通过 BufferedWriter 来将文本内容写入到文件中了。

​		下面我们就编写一个完整的例子，借此学习一下如何在 Android 项目中使用文本存储的技术。首先创建一个 FilePersistenceTest 项目，并修改 activity_main.xml 中的代码，如下所示：

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent" >  
  
    <EditText   
        android:id="@+id/edit"  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        android:hint="Type something here"  
        />  
  
</LinearLayout>
```

​		这里只是在布局中加入了一个 EditText，用于输入文本内容。其实现在你就可以运行一下程序了，界面上肯定会有一个文本输入框。然后在文本输入框中随意输入点什么内容，再按下 Back 键，这时输入的内容肯定就已经丢失了，因为它只是瞬时数据，在 Activity 被销毁后就会被回收。而这里我们要做的，就是在数据被回收之前，将它存储到文件当中。修改 MainActivity 中的代码，如下所示：

```java
public class MainActivity extends Activity {  
  
    private EditText edit;  
      
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        edit = (EditText) findViewById(R.id.edit);  
    }  
  
    @Override  
    protected void onDestroy() {  
        super.onDestroy();  
        String inputText = edit.getText().toString();  
        save(inputText);  
    }  
  
    public void save(String inputText) {  
        FileOutputStream out = null;  
        BufferedWriter writer = null;  
        try {  
            out = openFileOutput("data", Context.MODE_PRIVATE);  
            writer = new BufferedWriter(new OutputStreamWriter(out));  
            writer.write(inputText);  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (writer != null) {  
                    writer.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
} 
```

​		可以看到，首先我们在 onCreate() 方法中获取了 EditText 的实例，然后重写了 onDestroy() 方法，这样就可以保证在 Activity 销毁之前一定会调用这个方法。在 onDestroy() 方法中我们获取了 EditText 中输入的内容，并调用 save() 方法把输入的内容存储到文件中，文件命名为 data。save() 方法中的代码和之前的示例基本相同，这里就不再做解释了。

​		现在运行一下程序，并在 EditText 中输入一些内容，然后按下 Back 键关闭程序，这时我们输入的内容就已经保存到文件汇总了。那么如何才能证实数据确实已经保存成功了呢？我们可以借助 DDMS 的 File Explorer 来查看一下。切换到 DDMS 视图，并点击 File Explorer 选项卡，在这里进入到 /data/com.example.filepersistenttest/files/ 目录下，可以看到生成了一个 data 文件



### 从文件中读取数据

​		类似于将数据存储到文件中，Context 类中还提供了一个 openFileInput() 方法，用于从文件中读取数据。这个方法要比 openFileOutput() 简单一些，它只接收一个参数，即要读取的文件名，然后系统会自动到 `/data/data/<package name>/files/` 目录下去加载这个文件，并返回一个 FileInputStream 对象，得到这个对象之后再通过 Java 流的方式就可以将数据读取出来了。

以下是一段简单的代码示例，展示了如何从文件中读取文本数据：

```java
public String load() {  
    FileInputStream in = null;  
    BufferedReader reader = null;  
    StringBuilder content = new StringBuilder();  
    try {  
        in = openFileInput("data");  
        reader = new BufferedReader(new InputStreamReader(in));  
        String line = "";  
        while ((line = reader.readLine()) != null) {  
            content.append(line);  
        }  
    } catch (IOException e) {  
        e.printStackTrace();  
    } finally {  
        if (reader != null) {  
            try {  
                reader.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    return content.toString();  
}
```

​		在这段代码中，首先通过 openFileInput() 方法获取到了一个 FileInputStream 对象，然后借助它又构建出了一个 InputStreamReader 对象，接着再使用 InputStreamReader 构建出一个 BufferedReader 对象，这样我们就可以通过 BufferedReader 进行一行行地读取，把文件中所有的文本内容全部读取出来并存放在一个 StringBuilder 对象中，最后将读取到的内容返回就可以了。

​		了解了从文件中读取数据的方法，那么我们就来继续完善上一小节中的例子，使得重新启动程序时 EditText 中能够保留我们上次输入的内容。修改 MainActivity 中的代码，如下所示：

```java
public class MainActivity extends Activity {  
  
    private EditText edit;  
      
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        edit = (EditText) findViewById(R.id.edit);  
        String inputText = load();  
        if (!TextUtils.isEmpty(inputText)) {  
            edit.setText(inputText);  
            edit.setSelection(inputText.length());  
            Toast.makeText(this, "Restoring succeeded", Toast.LENGTH_SHORT).show();  
        }  
    }  
        ......  
  
    public String load() {  
        FileInputStream in = null;  
        BufferedReader reader = null;  
        StringBuilder content = new StringBuilder();  
        try {  
            in = openFileInput("data");  
            reader = new BufferedReader(new InputStreamReader(in));  
            String line = "";  
            while ((line = reader.readLine()) != null) {  
                content.append(line);  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (reader != null) {  
                try {  
                    reader.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return content.toString();  
    }  
  
}  
```

​		可以看到，这里的思路非常简单，在 onCreate() 方法中调用 load() 方法来读取文件中存储的文本内容，如果读到的内容不为空，就调用 EditText 的 setText() 方法将内容填充到 EditText 里，并调用 setSelection 方法将输入光标移动到文本的末尾位置以便于继续输入，然后弹出一句还原成功的提示。

​		**注意上述代码在对字符串进行非空判断的时候使用了TextUtils.isEmpty() 方法**，这是一个非常好用的方法，它可以一次性进行两种空值的判断。当传入的字符串等于 null 或者等于空字符串的时候，这个方法都会返回 true，从而使得我们不需要单独去判断这两种空值，再使用逻辑运算符连接起来了。

​		不过正如前面所说，文件存储的方式并不适合用于保存一些较为复杂的文本数据，因此，下面我们就来学习一下 Android 中的另一种数据持久化的方式，它比文件存储更加简单易用，而且可以很方便地对某一指定的数据进行读写操作。



------



## SharedPreferences 存储

​		不同于文件的存储方式，SharedPreferences 是使用键值对的方式来存储数据的。也就是说当保存一条数据的时候，需要给这条数据提供一个对应的键，这样在读取数据的时候就可以通过这个键把相应的值取出来。而且 SharedPreferences 还支持多种不同的数据类型存储，如果存储的数据类型是整型，那么读取出来的数据也是整型，存储的数据是一个字符串，读取出来的数据仍然是字符串。

​		这样你应该就能明显地感受到，使用 SharedPreferences 来进行数据持久化要比使用文件方便很多，下面我们就来看一下它的具体用法吧。

### 将数据存储到 SharedPreferences 中

​		要想使用 SharedPreferences 来存储数据，首先需要获取到 SharedPreferences 对象。Android 中主要提供了三种方法用于得到 SharedPreferences 对象。

> #### 1. Context 类中的 getSharedPreferences() 方法
>
> ​		此方法接收两个参数，第一个参数用于指定 SharedPreferences 文件的名称，如果指定的文件不存在则会创建一个，SharedPreferences 文件都是存放在 `/data/data/<package name>/shared_prefs/` 目录下的。第二个参数用于指定操作模式，主要有两种模式可以选择，MODE_PRIVATE 和 MODE_MULTI_PROCESS。MODE_PRIVATE 仍然是默认的操作模式，和直接传入 0 效果是相同的，表示只有当前的应用程序才可以对这个 SharedPreferences 文件进行读写。MODE_MULTI_PROCESS 则一般是用于会有多个进程中对同一个 SharedPreferences 文件进行读写的情况。类似地，MODE_WORLD_READABLE 和 MODE_WORLD_WRITEABLE 这两种模式已在 Android 4.2 版本中被废弃。
>
> #### 2. Activity 类中的 getPreferences() 方法
>
> ​		这个方法和 Context 中的 getSharedPreferences() 方法很相似，不过它只接收一个操作模式参数，因为使用这个方法时会自动将当前 Activity 的类名作为 SharedPreferences 的文件名。
>
> #### 3. PreferenceManager 类中的 getDefaultSharedPreferences() 方法
>
> ​		这是一个静态方法，它接收一个 Context 参数，并自动使用当前应用程序的包名作为前缀来命名 SharedPreferences 文件。

​		得到了 SharedPreferences 对象之后，就可以开始向 SharedPreferences 文件中存储数据了，主要可以分为三步实现。

1. 调用 SharedPreferences 对象的 edit() 方法来获取一个 SharedPreferences.Editor 对象。
2. 向 SharedPreferences.Editor 对象中添加数据，比如添加一个布尔型数据就使用 putBoolean 方法，添加一个字符串则使用 putString() 方法，以此类推。
3. 调用 commit() 方法将添加的数据提交，从而完成数据存储操作。



​		不知不觉中已经将理论知识介绍得挺多了，那我们就赶快通过一个例子来体验一下 SharedPreferences 存储的用法吧。新建一个 SharedPreferencesTest 项目，然后修改 activity_main.xml 中的代码，如下所示：

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent"  
    android:orientation="vertical" >  
      
    <Button   
        android:id="@+id/save_data"  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        android:text="Save data"  
        />  
      
</LinearLayout> 
```

​		这里我们不做任何复杂的功能，只是简单地放置了一个按钮，用于将一些数据存储到 SharedPreferences 文件当中。然后修改 MainActivity 中的代码，如下所示：

```java
public class MainActivity extends Activity {  
      
    private Button saveData;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        saveData = (Button) findViewById(R.id.save_data);  
        restoreData = (Button) findViewById(R.id.restore_data);  
        saveData.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();  
                editor.putString("name", "Tom");  
                editor.putInt("age", 28);  
                editor.putBoolean("married", false);  
                editor.commit();  
            }  
        });  
    }  
  
}  
```

​		可以看到，这里首先给按钮注册了一个点击事件，然后在点击事件中通过 getSharedPreferences() 方法指定 SharedPreferences 的文件名为 data，并得到了 SharedPreferences.Editor 对象。接着向这个对象中添加了三条不同类型的数据，最后调用 commit() 方法进行提交，从而完成了数据存储的操作。

​		可以看到，我们刚刚在按钮的点击事件中添加的所有数据都已经成功保存下来了，并且 SharedPreferences 文件是用 XML 格式来对数据进行管理的。

![img](README.assets/20160311100822897)



### 从 SharedPreferences 中读取数据

 		你应该已经感觉到了，使用 SharedPreferences 来存储数据是非常简单的，不过下面还有更好的消息，其实从 SharedPreferences 文件中读取数据更加的简单。SharedPreferences 对象中提供了一系列的 get 方法用于对存储的数据进行读取，每种 get 方法都对应 SharedPreferences.Editor 中的一种 put 方法，比如读取一个布尔型数据就使用 getBoolean() 方法，读取一个字符串就使用 getString() 方法。这些 get 方法都接收两个参数，第一个参数是键，传入存储数据时使用的键就可以得到相应的值了，第二个参数是默认值，即表示当传入的键找不到对应的值时，会以什么样的默认值进行返回。

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent"  
    android:orientation="vertical" >  
      
    <Button   
        android:id="@+id/save_data"  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        android:text="Save data"  
        />  
      
    <Button   
        android:id="@+id/restore_data"  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        android:text="Restore data"  
        />  
      
</LinearLayout>
```

​		这里增加了一个还原数据的按钮，我们希望通过点击这个按钮来从 SharedPreferences 文件中读取数据。修改 MainActivity 中的代码，如下所示：

```java
public class MainActivity extends Activity {  
      
    private Button saveData;  
      
    private Button restoreData;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        saveData = (Button) findViewById(R.id.save_data);  
        restoreData = (Button) findViewById(R.id.restore_data);  
        ......  
        restoreData.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);  
                String name = pref.getString("name", "");  
                int age = pref.getInt("age", 0);  
                boolean married = pref.getBoolean("married", false);  
                Log.d("MainActivity", "name is " + name);  
                Log.d("MainActivity", "age is " + age);  
                Log.d("MainActivity", "married is " + married);  
            }  
        });  
    }  
  
}  
```

​		可以看到，我们在还原数据按钮的点击事件中首先通过 getSharedPreferences() 方法得到了 SharedPreferences 对象，然后分别调用它的 getString()、getInt() 和 getBoolean() 方法去获取前面所存储的姓名、年龄和是否已婚，如果没有找到相应的值就会使用方法中传入的默认值来代替，最后通过 Log 将这些值打印出来。



------



## SQLite 数据库存储

​		SQLite 是一款轻量级的关系型数据库，**它的运算速度非常快，占用资源很少，通常只需要几百 K 的内存就足够了，因而特别适合在移动设备上使用**。SQLite 不仅支持标准的 SQL 语法，还遵循了数据库的 ACID 事务，所以只要你以前使用过其他的关系型数据库，就可以很快地上手 SQLite。而 SQLite 又比一般的数据库要简单得多，它甚至不用设置用户名和密码就可以使用。Android 正是把这种功能极为强大的数据库嵌入到了系统当中，使得本地持久化的功能有了一次质的飞跃。

​		前面我们所学的的文件存储和 SharedPreferences 存储毕竟只适用于去保存一些简单的数据和键值对，当需要存储大量复杂的关系型数据的时候，你就会发现以上两种存储方式很难应付得了。比如我们手机的短信程序中可能会有很多个会话，每个会话中又包含了很多条信息内容，并且大部分会话还可能各自对应了电话簿中的某个联系人。很难想象如何用文件或者 SharedPreferences 来存储这些数据量大、结构型复杂的数据吧？但是使用数据库就可以做到。那么我们就赶快来看一看，Android 中的 SQLite 数据库到底是如何使用的。

### 创建数据库

​		Android 为了让我们能够更加方便地管理数据库，专门提供了一个SQLiteOpenHelper 帮助类，借助这个类就可以非常简单地对数据库进行创建和升级。

​		首先你要知道 SQLiteOpenHelper 是一个抽象类，这意味着如果我们想要使用它的话，就需要创建一个自己的帮助类去继承它。SQLiteOpenHelper 中有两个抽象方法，分别是 onCreate() 和 onUpgrade()，我们必须在自己的帮助类里面重写这两个方法，然后分别在这两个方法中去实现创建、升级数据库的逻辑。

​		SQLiteOpenHelper 中还有两个非常重要的实例方法，getReadableDatabase() 和 getWritableDatabase()。这两个方法都可以创建或打开一个现有的数据库（如果数据库已存在则直接打开，否则创建一个新的数据库），并返回一个可对数据进行读写操作的对象。不同的是，**当数据库不可写入的时候（如磁盘空间已满）getReadableDatabase() 方法返回的对象将以只读的方式去打开数据库，而 getWritableDatabase() 方法则将出现异常。**

​		SQLiteOpenHelper 中有两个构造方法可供重写，**一般使用参数少一点的那个构造方法即可**。这个构造方法中接收四个参数，**第一个参数是Context，这个没什么好说的，必须要有它才能对数据库进行操作**。**第二个参数是数据库名，创建数据库时使用的就是这里指定的名称**。第三个参数允许我们在查询数据的时候返回一个自定义的 Cursor，一般都是传入 null。第四个参数表示当前数据库的版本号，可用于对数据库进行升级操作。构建出 SQLiteOpenHelper 的实例之后，再调用它的 getReadableDatabase() 或 getWritableDatabase() 方法就能够创建数据库了，数据库文件会存放在 `/data/data/<package name>/databases/`目录下。此时，重写的 onCreate() 方法也会得到执行，所以通常会在这里去处理一些创建表的逻辑。

​		接下来还是让我们通过例子的方式来更加直观地体会 SQLiteOpenHelper 的用法吧，首先新建一个 DatabaseTest 项目。

​		这里我们希望创建一个名为 BookStore.db 的数据库，然后在这个数据库中新建一张 Book 表，表中有 id（主键）、作者、杰哥、页数和书名等列。创建数据库表当然还是需要用建表语句的，这里也是要考验一下你的 SQL 基本功了，Book 表的建表语句如下所示：

```java
Create table Book (  
    id integer primary key autoincrement,  
    author text,  
    price real,  
    pages integer,  
    name text 
)  
```

​		SQLite 不像其他的数据库拥有众多繁杂的数据类型，它的数据类型很简单，**integer 表示整型，real 表示浮点型，text 表示文本类型，blob 表示二进制类型**。另外，上述建表语句中我们还是用了 **primary key 将 id 列设为主键，并用 autoincrement 关键字表示 id 列是自增长的**。

​		然后需要在代码中去执行这条 SQL 语句，才能完成创建表的操作。新建 MyDatabaseHelper 类继承自 SQLiteOpenHelper，代码如下所示：

```java
public class MyDatabaseHelper extends SQLiteOpenHelper {  
  
    public static final String CREATE_BOOK = "create table Book ("  
            + "id integer primary key autoincrement, "   
            + "author text, "  
            + "price real, "   
            + "pages integer, "   
            + "name text)";  
      
    private Context mContext;  
  
    public MyDatabaseHelper(Context context, String name,  
            CursorFactory factory, int version) {  
        super(context, name, factory, version);  
        mContext = context;  
    }  
  
    @Override  
    public void onCreate(SQLiteDatabase db) {  
        db.execSQL(CREATE_BOOK);  
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();  
    }  
  
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
    }  
  
} 
```

​		可以看到，我们把建表语句定义成了一个字符串常量，然后在 onCreate() 方法中又调用了 SQLiteDatabase 的 execSQL() 方法去执行这条建表语句，并弹出一个 Toast 提示创建成功，这样就可以保证在数据库创建完成的同事还能成功创建 Book 表。

现在修改 activity_main.xml 中的代码，如下所示：

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent"  
    android:orientation="vertical" >  
  
    <Button  
        android:id="@+id/create_database"  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        android:text="Create database"  
         />  
  
</LinearLayout> 
```

​		布局文件很简单，就是加入了一个按钮，用于创建数据库。最后修改 MainActivity 中的代码，如下所示：

```java
public class MainActivity extends Activity {  
  
    private MyDatabaseHelper dbHelper;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);  
        Button createDatabase = (Button) findViewById(R.id.create_database);  
        createDatabase.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                dbHelper.getWritableDatabase();  
            }  
        });  
    }  
  
} 
```

​		这里我们在 onCreate() 方法中构建了一个 MyDatabaseHelper 对象，并且通过构造函数的参数将数据库名指定为 BookStore.db，版本号指定为 1，然后在 Create database 按钮的点击事件里调用了 getWritableDatabase() 方法。这样当第一次点击 Create database 按钮时，就会检测到当前程序中没有 BookStore.db 这个数据库，于是会创建该数据库并调用 MyDatabaseHelper 中的 onCreate() 方法，这样 Book 表也就得到了创建，然后会弹出一个 Toast 提示创建成功，再次点击 Create database 按钮时，会发现此时已经存在 BookStore.db 数据库了，因此不会再创建一次。

​		怎么样才能证实它们的确是创建成功了？如果还是使用 File Explorer，那么最多你只能看到 databases 目录下出现了一个 BookStore.db 文件，Book 表示无法通过 File Explorer 看到的。因此这次我们准备换一种查看方式，使用 adb shell 来对数据库和表的创建情况进行检查。

​		**adb 是 Android SDK 中自带的一个调试工具**，使用这个工具可以直接对连接在电脑上的手机或模拟器进行调试操作。它存放在 sdk 的 platform-tools 目录下，如果想要在命令行中使用这个工具，就需要先把它的路径配置到环境变量里。



### 升级数据库

​		如果你足够细心，一定会发现 MyDatabaseHelper 中还有一个空方法呢！没错，onUpgrade() 方法是用于对数据库进行升级的，它在整个数据库的管理工作当中起着非常重要的作用，可千万不能忽视它哟。

​		目前 DatabaseTest 项目中已经有一张 Book 表用于存放书的各种详细数据，如果我们想再添加一张 Category 表用于记录书籍的分类该怎么做呢？

​		比如 Category 表汇总有 id（主键）、分类名和分类代码这几个列，那么建表语句就可以写成：

```java
create table Category (  
    id integer primary key autoincrement,  
    category_name text,  
    category_code integer
)  
```

接下来我们将这条建表语句添加到 MyDatabaseHelper 中，代码如下所示：

```java
public class MyDatabaseHelper extends SQLiteOpenHelper {  
  
    public static final String CREATE_BOOK = "create table Book ("  
            + "id integer primary key autoincrement, "   
            + "author text, "  
            + "price real, "   
            + "pages integer, "   
            + "name text)";  
      
    public static final String CREATE_CATEGORY = "create table Category ("  
            + "id integer primary key autoincrement, "  
            + "category_name text, "  
            + "category_code integer)";  
  
    private Context mContext;  
  
    public MyDatabaseHelper(Context context, String name,  
            CursorFactory factory, int version) {  
        super(context, name, factory, version);  
        mContext = context;  
    }  
  
    @Override  
    public void onCreate(SQLiteDatabase db) {  
        db.execSQL(CREATE_BOOK);  
        db.execSQL(CREATE_CATEGORY);  
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();  
    }  
  
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
    }  
  
} 
```

​		看上去好像都挺对的吧，现在我们重新运行一下程序，并点击 Create database 按钮，咦？竟然没有弹出创建成功的提示。当然，你也可以通过 adb 工具到数据库中再去检查一下，这样你会更加地确认，Category 表没有创建成功！

​		其实没有创建成功的原因不难思考，因为此时 BookStore.db 数据库已经存在了，之后不管我们怎样点击 Create database 按钮，MyDatabaseHelper 中的 onCreate() 方法都不会再次执行，因此新添加的表也就无法得到创建了。

​		解决这个问题的办法也相当简单，只需要先将程序卸载掉，然后重新运行，这时 BookStore.db 数据库已经不存在了，如果再点击 Create database 按钮，MyDatabaseHelper 中的 onCreate() 方法就会执行，这时 Category 表就可以创建成功了。

​		不过通过卸载程序的方式来新增一张表毫无疑问是很极端的做法，其实我们只需要巧妙地运用 SQLiteOpenHelper 的升级功能就可以很轻松地解决这个问题。修改 MyDatabaseHelper 中的代码，如下所示：

```java
public class MyDatabaseHelper extends SQLiteOpenHelper {  
    ......  
  
       @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
        db.execSQL("drop table if exists Book");  
        db.execSQL("drop table if exists Category");  
        onCreate(db);  
    }  
  
} 
```

​		可以看到，我们在 onUpgrade() 方法中执行了两天 DROP 语句，如果发现数据库中已经存在 Book 表或 Category 表了，就讲这两张表删除掉，然后再调用 onCreate() 方法去重新创建。这里先将已经存在的表删除掉，是因为如果在创建表时发现这张表已经存在了，就会直接报错。

​		接下来的问题就是如何让 onUpgrade() 方法能够执行了，还记得 SQLiteOpenHelper 的构造方法里接收的第四个参数吗？它表示当前数据库的版本号，之前我们传入的是 1，现在只要传入一个比 1 大的书，就可以让 onUpgrade() 方法得到执行了。修改 MainActivity 中的代码，如下所示：

```java
public class MainActivity extends Activity {  
  
    private MyDatabaseHelper dbHelper;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);  
        Button createDatabase = (Button) findViewById(R.id.create_database);  
        createDatabase.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                dbHelper.getWritableDatabase();  
            }  
        });  
    }  
  
}  
```



### 添加数据

​		 现在你已经掌握了创建和升级数据库的方法，接下来就该学习一下如何对表中的数据进行操作了。其实我们可以对数据进行的操作也就无非四种，即 CRUD。其中 C 代表添加 （Create），R 代表查询（Retrieve），U 代表更新（Update），D 代表删除（Delete）。每一种操作又各自对应了一种 SQL 命令，如果你比较熟悉 SQL 语言的话，一定会知道添加数据时使用 insert，查询数据时使用 select，更新数据时使用 update，删除数据时使用 delete。但是开发者的水平总会是参差不齐的，未必每一个人都能非常熟悉地使用 SQL 语言，因此Android 也是提供了一系列的辅助性方法，使得在 Android 中即使不去编写 SQL 语句，也能轻松完成所有的 CRUD 操作。

​		前面我们已经知道，调用 SQLiteOpenHelper 的 getReadableDatabase() 或 getWritableDatabase() 方法是可以用于创建和升级数据库的，不仅如此，这两个方法还都会返回一个 SQLiteDatabase 对象，借助这个对象就可以对数据进行 CRUD 操作了。

​		那么我们一个一个功能地看，首先学习一下如何向数据库的表中添加数据吧。SQLiteDatabase 中提供了一个 insert() 方法，这个方法就是专门用于添加数据的。它接收三个参数，第一个参数是表名，我们希望向哪张表里添加数据，这里就传入该表的名字。第二个参数用于在未指定添加数据的情况下给某些可为空的列自动赋值 NULL，一般我们用不到这个功能，直接传入 null 即可。第三个参数是一个 ContentValues 对象，它提供了一系列的 put() 方法重载，用于向 ContentValues 中添加数据，只需要将表中的每个列名已经相应的待添加数据传入即可。

​		介绍完了基本用法，接下来还是让我们通过例子的方式来亲身体验一下如何添加数据吧。修改 activity_main.xml 中的代码，如下所示：

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent"  
    android:orientation="vertical" >  
  
    ......  
      
    <Button   
        android:id="@+id/add_data"  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        android:text="Add data"  
        />  
  
</LinearLayout>
```

​		可以看到，我们在布局文件中又新增了一个按钮，稍后就会在这个按钮的点击事件里编写添加数据的逻辑。接着修改 MainActivity 中的代码，如下所示：

```java
public class MainActivity extends Activity {  
  
    private MyDatabaseHelper dbHelper;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);  
        ......  
        Button addData = (Button) findViewById(R.id.add_data);  
        addData.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                SQLiteDatabase db = dbHelper.getWritableDatabase();  
                ContentValues values = new ContentValues();  
                                // 开始组装第一条数据  
                               values.put("name", "The Da Vinci Code");  
                values.put("author", "Dan Brown");  
                values.put("pages", 454);  
                values.put("price", 16.96);  
                db.insert("Book", null, values); // 插入第一条数据  
                values.clear();  
                                 // 开始组装第二条数据  
                               values.put("name", "The Lost Symbol");  
                values.put("author", "Dan Brown");  
                values.put("pages", 510);  
                values.put("price", 19.95);  
                db.insert("Book", null, values);  // 插入第二条数据  
            }  
        });  
    }  
  
} 
```

在添加数据按钮的点击事件里面，我们先获取到了 SQLiteDatabase 对象，然后使用 ContentValues 来对要添加的数据进行组装。如果你比较细心的话应该会发现，这里只对 Book 表里其中四列的数据进行了组装，id 那一列病没有给它赋值。这是因为在前面创建表的时候我们就将 id 列设置为自增长了，它的值会在入库的时候自动生成，所以不需要手动给它赋值了。



### 更新数据

​		学习完了如何向表中添加数据，接下来我们看看怎样才能修改表中已有的数据。SQLiteDatabase 中也是提供了一个非常好用的 update() 方法用于对数据进行更新，这个方法接收四个参数，第一个参数和 insert() 方法一样，也是表名，在这里指定去更新哪张表里的数据。第二个参数是 ContentValues 对象，要把更新数据在这里组装进去。第三、第四个参数用于去约束更新某一行或某几行中的数据，不指定的话默认就是更新所有行。

​		那么接下来我们仍然是在 DatabaseTest 项目的基础上修改，看一下更新数据的具体用法。比如说刚才添加到数据库里的第一本书，由于过了畅销季，卖得不是很火了，现在需要通过降低价格的方式来吸引更多的顾客，我们应该怎么操作呢？首先修改 activity_main.xml 中的代码，如下所示：

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent"  
    android:orientation="vertical" >  
  
    ......  
      
    <Button   
        android:id="@+id/update_data"  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        android:text="Update data"  
        />  
    
</LinearLayout> 
```

​		布局文件中的代码就已经非常简单了，就是添加了一个用于更新数据的按钮。然后修改 MainActivity 中的代码，如下所示：

```java
public class MainActivity extends Activity {  
  
    private MyDatabaseHelper dbHelper;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);  
        ......  
        Button updateData = (Button) findViewById(R.id.update_data);  
        updateData.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                SQLiteDatabase db = dbHelper.getWritableDatabase();  
                ContentValues values = new ContentValues();  
                values.put("price", 10.99);  
                db.update("Book", values, "name = ?",  
                        new String[] { "The Da Vinci Code" });  
            }  
        });  
    }  
  
} 
```

​		这里在更新数据按钮的点击事件里面构建了一个 ContentValues 对象，并且只给它指定了一组数据，说明我们只是想把价格这一列的数据更新成 10.99。然后调用了 SQLiteDatabase 的 update() 方法去执行具体的更新操作，可以看到，这里使用了第三、第四个参数来指定具体更新哪几行。**第三个参数对应是的 SQL语句的 where 部分，表示去更新所有 name 等于 ? 的行，而？是一个占位符，可以通过第四个参数提供的一个字符串数组为第三个参数中的每个占位符指定相应的内容**。因此上述代码想表达的意图就是，将名字是 The Da Vinci Code 的这本书的价格改成 10.99。



### 删除数据

​		删除数据对你来说应该就更简单了，因为它所需要用到的知识点你全部已经学过了。SQLiteDatabase 中提供了一个 delete() 方法专门用于删除数据，这个方法接收三个参数，第一个参数仍然是表名，这个已经没什么好说的了，第二、第三个参数又是用于去约束删除某一行或某几行的数据，不指定的话默认就是删除所有行。

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent"  
    android:orientation="vertical" >  
  
    ......  
   
   <Button   
        android:id="@+id/delete_data"  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        android:text="Delete data"  
        />  
  
</LinearLayout>  
```

仍然是在布局文件中添加了一个按钮，用于删除数据。然后修改 MainActivity 中的代码，如下所示：

```java
public class MainActivity extends Activity {  
  
    private MyDatabaseHelper dbHelper;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);  
        ......  
        Button deleteButton = (Button) findViewById(R.id.delete_data);  
        deleteButton.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                SQLiteDatabase db = dbHelper.getWritableDatabase();  
                db.delete("Book", "pages > ?", new String[] { "500" });  
            }  
        });  
    }  
  
} 
```

​		可以看到，我们在删除按钮的点击事件里指明去删除 Book 表中的数据，并且通过第二、第三个参数来指定仅删除那些页数超过 500 页的书籍。当然这个需求很奇怪，这里也仅仅是为了做个测试。你可以先查看一下当前 Book 表里的数据，其中 The Lost Symbol 这本书的页数超过了 500 页，也就是说当我们点击删除按钮时，这条记录应该会被删除掉。



### 查询数据

​		我们都知道 SQL 的全称是 Structured Query Language，翻译成中文就是结构化查询语言。它的大部分功能都是体现在“查”这个字上的，而“增删改”只是其中的一小部分功能。由于 SQL 查询涉及的内容实在是太多了，因此在这里只会介绍 Android 上的查询功能。

​		相信你已经猜到了，SQLiteDatabase 中还提供了一个 query() 方法用于对数据进行查询。这个方法的参数非常复杂，最短的一个方法重载也需要传入七个参数。那我们就先来看一下这七个参数格子的含义吧，**第一个参数不用说，当然还是表名，表示我们希望从哪张表中查询数据。第二个参数用于指定去查询哪几列，如果不指定则默认查询所有列。第三、第四个参数用于去约束查询某一行或几行的数据，不指定则默认是查询所有行的数据。第五个参数用于指定需要去 group by 的列，不指定则表示不对查询结果进行 group by 操作。第六个参数用于对 group by 之后的数据进行进一步的过滤，不指定则表示不进行过滤。第七个参数用于指定查询结果的排序方式，不指定则表示使用默认的排序方式**。更多详细的内容可以参考下表。其他几个 query() 方法的重载其实也大同小异，你可以自己去研究一下，这里就不再进行介绍了。

![img](README.assets/20160311145753786)

​		虽然 query() 方法的参数非常多，但是不要对它产生畏惧，因为我们不必为每条查询语句都指定上所有的参数，多数情况下只需要传入少数几个参数就可以完成查询操作了。**调用 query() 方法后会返回一个 Cursor 对象，查询到的所有数据都将从这个对象中取出**。

下面还是让我们通过例子的方式来体验一下查询数据的具体用法，修改 activity_main.xml 中的代码，如下所示：

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent"  
    android:orientation="vertical" >  
    ......  
      
    <Button   
        android:id="@+id/query_data"  
        android:layout_width="match_parent"  
        android:layout_height="wrap_content"  
        android:text="Query data"  
        />  
  
</LinearLayout>  
```

这个已经没什么好说的了，添加了一个按钮用于查询数据。然后修改 MainActivity 中的代码，如下所示：

```java
public class MainActivity extends Activity {  
  
    private MyDatabaseHelper dbHelper;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.activity_main);  
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);  
        ......  
        Button queryButton = (Button) findViewById(R.id.query_data);  
        queryButton.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
                SQLiteDatabase db = dbHelper.getWritableDatabase();  
                                 // 查询 Book 表中所有的数据  
                               Cursor cursor = db.query("Book", null, null, null, null, null,  
                        null);  
                if (cursor.moveToFirst()) {  
                    do {  
                                                // 遍历 Cursor 对象，取出数据并打印  
                                               String name = cursor.getString(cursor  
                                .getColumnIndex("name"));  
                        String author = cursor.getString(cursor  
                                .getColumnIndex("author"));  
                        int pages = cursor.getInt(cursor  
                                .getColumnIndex("pages"));  
                        double price = cursor.getDouble(cursor  
                                .getColumnIndex("price"));  
                        Log.d("MainActivity", "book name is " + name);  
                        Log.d("MainActivity", "book author is " + author);  
                        Log.d("MainActivity", "book pages is " + pages);  
                        Log.d("MainActivity", "book price is " + price);  
                    } while (cursor.moveToNext());  
                }  
                cursor.close();  
            }  
        });  
    }  
  
} 
```

​		可以看到，我们首先在查询的点击事件里面调用了 SQLiteDatabase 的 query() 方法去查询数据。这里的 query() 方法非常简单，只是使用了第一个参数指明去查询的 Book 表，后面的参数全部为 null。这就表示希望查询这张表中的所有数据，虽然这张表中目前只剩下一条数据了。查询完之后就得到一个 Cursor 对象，接着我们调用它的 moveToFirst() 方法将数据指针移动到第一行的位置，然后进入了一个循环中，去遍历查询的每一行数据。在这个循环中可以通过 Cursor 的 getColumIndex() 方法获取到某一列在表中对应的位置索引，然后将这个索引传入到相应的取值方法中，就可以得到从数据库中读取到的数据了。接着我们使用 Log 的方式将取出的数据打印出来，借此来检查一下读取工作有没有完成。最后别忘了调用 close() 方法来关闭 Cursor。



### 使用 SQL 操作数据库

​		虽然 Android 已经给我们提供了很多非常方便的 API 用于操作数据库，不过总会有一些人不习惯去使用这些辅助性的方法，而是更加青睐于直接使用 SQL 来操作数据库。

下面我就来简略演示一下，如何直接使用 SQL 来完成前面几小节中学过的 CRUD 操作。

添加数据的方法如下：

```java
db.execSQL("insert into Book (name, author, pages, price) values (?, ?, ?, ?)",new String[] { "The Da Vinci Code", "Dan Brown", "454", "16.96" });  
<pre name="code" class="java">db.execSQL("insert into Book (name, author, pages, price) values (?, ?, ?, ?)",new String[] { "The Lost Symbol", "Dan Brown", "510", "19.95" });
```

 更新数据的方法如下：

```java
db.execSQL("update Book set price = ? where name = ?", new String[] { "10.99","The Da Vinci Code" });  
```

删除数据的方法如下：

```java
db.execSQL("delete from Book where pages > ?", new String[] { "500" });  
```

查询数据的方法如下：

```java
db.rawQuery("select * from Book", null);  
```

​    可以看到，除了查询数据的时候调用的是 SQLiteDatabase 的 rawQuery() 方法，其他的操作都是调用的 execSQL() 方法。