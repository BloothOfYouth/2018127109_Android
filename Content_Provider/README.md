# 跨程序共享数据-探究内容提供器

文件存储，SharedPreferences存储以及数据库存储，使用这些持久化技术所保存的数据都只能在当前应用程序中访问。

## 内容提供器简介

内容提供器(Content Provider)主要用于在不同的应用程序之间实现数据共享的功能，它提供了一套完整的机制，允许一个程序访问另一个程序中的数据，同时还能保证被访数据的安全性。目前，使用内容提供器是Android实现跨程序共享数据的标准方式。

不同于文件存储和SharedPreferences存储中的两种全局可读写操作模式，内容提供器可以选择只对哪一部分数据进行共享，从而保证我们程序中的隐私数据不会有泄露的风险。



------



## 运行时权限

`Android`的权限机制并不是什么新鲜事物，从系统的第一个版本开始就已经存在了。

用户不需要在安装软件的时候一次性授权所有申请的权限，而是可以在软件的使用过程中再对某一项权限申请进行授权。

`Android`现在将所有的权限归成了两类，一类是`普通权限`，一类是`危险权限`。

**`普通权限`**指的是那些不会直接威胁到用户的安全和隐私的权限，对于这部分的权限申请，系统会自动帮我们进行授权，而不需要用户再去手动操作了。

**`危险权限`**则表示那些可能会触及用户隐私，或者对设备安全性造成影响的权限，如获取设备联系人信息等，对于这部分权限申请，必须要由用户手动点击授权才可以，否则程序就无法使用相应的功能。

危险权限总共就那么几个，除了危险权限之外，剩余的就都是普通权限了。

另外注意一下，表格中每个危险权限都属于一个权限组，我们在进行运行时权限处理时使用的是权限名，但是用户一旦同意授权了，那么该权限所对应的的权限组中所有的其他权限也会同时被授权。



### 在程序运行时申请权限

`CALL_PHONE`这个权限是编写拨打电话功能的时候需要声明的，因为拨打电话会涉及用户手机的资费问题，因而被列为了危险权限。

```java
button_make_call.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:10086"));
                    startActivity(intent);
                }catch (SecurityException e)
                {
                    e.printStackTrace();
                }
            }
        });
        
//声明权限
<uses-permission android:name="android.permission.CALL_PHONE"/>
```

我们构建了一个隐式`Intent`,`Intent`的`action`指定为`Intent.ACTION_CALL`。这是一个系统内置的打电话的动作，然后在`Data`部分指定了协议是`tel`,号码是`10086`。我们以前指定的`action`是`Intent.ACTION.DIAL`。表示打开拨号界面，这个是不需要声明权限的，而`Intent.ACTION_CALL`则可以直接拨打电话，因此必须声明权限。另外为了防止程序崩溃，我们将所有操作都放在了异常捕获代码块当中。

```java
public void initEvent()
    {
        button_make_call.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},1);
                }
                else
                {
                    Call();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
       switch (requestCode)
       {
           case 1:
               if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
               {
                   Toast.makeText(MainActivity.this, "你同意了相关权限！", Toast.LENGTH_SHORT).show();
                   Call();
               }
               else
               {
                   Toast.makeText(MainActivity.this, "你拒绝了相关权限！", Toast.LENGTH_SHORT).show();
               }
               break;

           default:
       }
    }

    private void Call()
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10086"));
            startActivity(intent);
        }catch (SecurityException e)
        {
            e.printStackTrace();
        }
    }
```

说白了，运行时权限的核心就是在程序运行过程中由用户授权我们去执行某些危险操作，程序是不可以擅自做主去执行这些危险操作的。因此，第一步就是要先判断用户是不是给过我们授权了，借助的是`ContextCompat.checkSelfPermission()`方法。`checkSelfPermission()`方法接收两个参数，第一个参数是`Context`,第二参数是具体的`权限名`，比如打电话的权限名就是`Manifest.permission.CALL_PHONE`。然后我们使用方法的返回值和`PackageManager.PERMISSION_GRANTED`作比较，相等就说明用户已经授权，不等就表示用户没有授权。

如果没有授权的话，则需要调用`ActivityCompat.requestPermissions()`方法来向用户申请授权，`requestPermissions()`方法接收三个参数第一个参数要求是`Activity的实例`，第二个参数是一个`String数组`，我们把要申请的权限名放在数组中即可，第三个参数是`请求码`，只要是唯一值就可以了。

调用完了`requestPermissions()`方法之后，系统会弹出一个权限申请的对话框，然后用户可以选择同意或拒绝我们的权限申请，不论是哪种结果，最终都会回调到`onRequestPermissionsResult()`方法中，而授权的结果则会封装在`grantResults`参数当中。这里只需要判断一下最后的授权结果，如果用户同意的话，就调用`Call()`方法来拨打电话，如果拒绝的话，我们只能放弃操作，并且弹出一条失败提示。



------



## 访问其他程序中的数据

内容提供器的用法一般有两种：一种是使用现有的内容提供器来读取和操作相应程序中的数据，另一种是创建自己的内容提供器给我们程序的数据提供外部访问接口。

如果一个应用程序通过内容提供器对其数据提供了外部访问的接口，那么任何其他的应用程序就都可以对这部分数据进行访问。

### ContentResolver的基本用法

对于每一个应用程序来说，如果想要访问内容提供器中共享的数据，就一定要借助ContentResolver类，可以通过Context中的getContentResolver方法获取到该类的实例。ContentResolver中提供了一系列的方法用于对数据进行CRUD操作。

不同于SQLiteDatabase,ContentResolver中的增删改查方法都是不接收表名参数的，而是使用一个Uri参数代替，这个参数被称为内容URI。内容URI给内容提供器中的数据建立了唯一标识符，它主要有两部分组成：authority和path。authority是用于对不同的应用程序做区分的，一般为了避免冲突，都会采用程序包名的方式来进行命名。path则是用于对同一应用程序中不同的表做区分的，通常都会添加到authority的后面。
 不过目前还很难辨认出这两个字符串就是两个内容URI，我们还需要在字符串的头部加上协议声明。因此，内容URI最标准的格式写法如下：
`content://com.example.app.provider/table1`

`content://com.example.app.provider/table2`

在得到了内容URI字符串之后，我们还需要将它解析成Uri对象才可以作为参数传入，解析的方法也非常简单，

`Uri uri = Uri.parse("content://com.example.app.provider/table1")`

只需要调用Uri.parse()方法，就可以将内容URI字符串解析成Uri对象了。

```java
Cursor cursor = getContentResolver().query(
uri,
projection,
selection,
selectionArgs,
sortOrder);
```

| query()方法参数 | 对应SQL部分               | 描述                             |
| --------------- | ------------------------- | -------------------------------- |
| uri             | from table_name           | 指定查询某个应用程序下的某一张表 |
| projection      | select column1, column2   | 指定查询的列名                   |
| selection       | where column = value      | 指定where的约束条件              |
| selectionArgs   | -                         | 为where中的占位符提供具体的值    |
| sortOrder       | order by column1, colunm2 | 指定查询结果的排序方式           |

查询完成后，返回的仍然是一个Cursor对象，这时我们就可以将数据从Cursor对象中逐个读取出来了。

```java
if (cursor != null) {
  while (cursor.moveToNext()) {
    String column1 = cursor.getString(cursor.getColumnIndex("column1"));
    int column2 = cursor.getInt.getColumnInt("column2");
  }
  cursor.close();
}
```

如何向表中添加一条数据：

```java
ContentValues values = new ContentValues();
values.put("column1","text");
values.put("column2",1);
getContentResolver().insert(uri,values);
```

> 将待添加的数据组装到ContentValues中，然后调用ContentResolver的insert()方法，将Uri和ContentValues作为参数传入即可。

如何向表中更新数据

```java
ContentValues values = new ContentValues();
values.put("column1","");
getContentResolver().update(uri,values,"column1 = ? and colum2 = ?",new String[]{"text","1"});
```

> 注意上述代码使用了selection和selectionArgs参数来对想要更新的数据进行约束，以防止所有的行都会受影响。

向表中删除数据

```java
getContentResolver().delete(uri,"column2 = ?",new String[]{"1"});
```



### 读取系统联系人

```dart
public class MainActivity extends AppCompatActivity
{

    ArrayAdapter<String> adapter;
    List<String> contactsList = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,
                contactsList);
        listView.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission
            .READ_CONTACTS},1);
        }
        else
        {
            readContacts();
        }
    }

    public void initView()
    {
        listView = (ListView) findViewById(R.id.contacts_view);
    }

    private void readContacts()
    {
        Cursor cursor = null;
        try
        {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,null,null,null);
            if (cursor != null)
            {
                while (cursor.moveToNext())
                {
                    String displayName = cursor.getString(cursor.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex
                            (ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contactsList.add(displayName + "\n" + number);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            cursor.close();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    readContacts();
                break;
            default:
        }
    }


}
```

获取ListView控件的实例，并给它设置好了适配器，然后开始调用运行时权限的处理逻辑，因为READ_CONTACTS权限是属于危险权限的。我们在用户授权之后调用readContacts()方法来读取系统联系人信息。

这里使用了ContentResolver的query()方法是来查询系统的联系人数据。

ContactsContract.CommonDataKinds.Phone类已经帮我们做好了封装，提供了一个CONTENT_URI常量，而这个常量就是使用Uri.parse()方法解析出来的结果。接着我们对Cursor对象进行遍历，将联系人姓名和手机号这些数据逐个取出，联系人姓名这一列对应的常量是ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME

联系人手机号这一列对应的常量是ContactsContract.CommonDataKinds.Phone.NUMBER。两个数据都取出之后，将它们进行拼接，并在中间加上换行符，然后将拼接后的数据添加到ListView的数据源里，并通知刷新一下ListView，最后千万不要忘记将Cursor对象关闭掉。

**读取系统联系人的权限千万不能忘记声明**。

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.hjf.content_provider">
  
		    <uses-permission android:name="android.permission.READ_CONTACTS"></uses-permission>

  ...
  
</manifest>
```



------



## 创建内容提供器的步骤

如果想要实现跨程序共享数据的功能，官方推荐的方式就是使用内容提供器，可以通过新建一个类去继承ContentProvider的方式来创建一个自己的内容提供器。ContentProvider类中有6个抽象方法，我们在使用子类继承它的时候，需要将这6个方法全部重写。

```java
public class MyProvider extends ContentProvider
{


    @Override
    public boolean onCreate()
    {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        return 0;
    }
    
}
```

**1.onCreate()**

初始化内容提供器的时候调用，通常会在这里完成对数据库的创建和升级等操作，返回true表示内容提供器初始化成功，返回false则表示失败。注意，只有当存在ContentResolver尝试访问我们程序中的数据时，内容提供器才会被初始化。

**2.query()**

从内容提供器中查询数据。使用uri参数来确定查询哪张表，projection参数用于确定查询哪些列，selection和selectionArgs参数用于约束查询哪些行，sortOrder参数用于对结果进行排序，查询的结果保存在Cursor对象中返回。

**3.insert()**

向内容提供器中添加一条数据。使用uri参数来确定要添加到的表，待添加的数据保存在values参数中。添加完成后，返回一个用于表示这条新纪录的URI。

**4. update()**

更新内容提供器中已有的数据。使用uri参数来确定更新哪一张表中的数据。新数据保存在values参数中，selection和selectionArgs参数用于约束更新哪些行，受影响的行数将作为返回值返回。

**5.delete()**

从内容提供器中删除数据。使用uri参数确定删除哪一张表中的数据，selection和selectionArgs参数用于约束删除哪些行，被删除的行数将作为返回值返回。

**6.getType()**

根据传入的内容URI来返回相应的MIME类型。



以路径结尾就表示期望访问该表中所有的数据，以id结尾就表示期望访问该表中拥有相应id的数据。我们可以使用通配符的方式来分别匹配这两种格式的内容URI，规则如下：

***：表示匹配任意长度的任意字符**。

**#：表示匹配任意长度的数字**。

所以一个能够匹配任意表的内容URI格式就可以写成：

`content://com.example.app.provider/*`

而一个能够匹配table1表中任意一行数据的内容URI格式就可以写成：

`content://com.example.app.provider/table1/#`

我们再借助UriMatcher这个类就可以轻松地实现匹配内容URI的功能。UriMatcher中提供了一个addURI()方法，这个方法接受3个参数，可以分别把authority,path和一个自定义代码传进去。

当调用UriMatcher的match()方法时，将可以将一个Uri对象传入，返回值是某个能够匹配这个Uri对象所对应的自定义代码，利用这个代码，我们就可以判断出调用方期望访问的是哪张表中的数据了。

```java
public class MyProvider extends ContentProvider
{
    public static final int TABLE1_DIR = 0;

    public static final int TABLE1_ITEM = 1;

    public static final int TABLE2_DIR = 2;

    public static final int TABLE2_ITEM = 3;

    private static UriMatcher uriMatcher;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.app.provider","table1",TABLE1_DIR);
        uriMatcher.addURI("com.example.app.provider","table1/#",TABLE1_ITEM);
        uriMatcher.addURI("com.example.app.provider","table2",TABLE2_DIR);
        uriMatcher.addURI("com.example.app.provider","table2/#",TABLE2_ITEM);
    }

    @Override
    public boolean onCreate()
    {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        switch (uriMatcher.match(uri))
        {
            case TABLE1_DIR:
                //查询table表中的所有数据
                break;
            case TABLE1_ITEM:
                //查询table1表中的单条数据
                break;
            case TABLE2_DIR:
                //查询table2表中的所有数据
                break;
            case TABLE2_ITEM:
                //查询table2表中的单条数据
                break;
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        return 0;
    }

}
```

MyProvider中新增了4个整形常量。其中TABLE1_DIR表示访问table1表中的所有数据，   TABLE1_ITEM表示访问table1表中的单条数据，TABLE2_DIR表示访问table2表中的所有数据，
 TABLE2_ITEM表示访问table2表中的单条数据。

接着在静态代码块里我们创建了UriMatcher的实例，并调用addURI()方法将期望匹配的内容URI格式传递进去，注意这里传入的路径参数是可以使用通配符的。

然后当query()方法被调用的时候就会通过UriMatcher的match()方法对传入的Uri对象进行匹配，如果发现UriMatcher中某个内容URI格式成功匹配了该Uri对象，则会返回相应的自定义代码，然后我们就可以判断出调用方期望访问的到底是什么数据了。

还有一个方法你会比较陌生，即**getType()方法**。它是所有的内容提供器都必须提供的一个方法，用于获取Uri对象所对应的的MIME类型。一个内容URI所对应的MIME字符串主要有3部分构成。

1. **必须以vnd开头**

2. **如果内容URI以路径结尾，则后接`android.cursor.dir/`,如果内容URI以id结尾，则后接`android.cursor.item/`**

3. **最后接上`vnd.<authority>.<path>`**

对于`content://com.example.app.provider/table1`这个内容URI,它所对应的MIME类型就可以写成：

**`vnd.android.cursor.dir/vnd.com.example.app.provider.table1`**

对于`content://com.example.app.provider/table1/1`这个内容URI,它所对应的MIME类型就可以写成：

**`vnd.android.cursor.item/vnd.com.example.app.provider.table1`**

```java
public class MyProvider extends ContentProvider
{
    public static final int TABLE1_DIR = 0;

    public static final int TABLE1_ITEM = 1;

    public static final int TABLE2_DIR = 2;

    public static final int TABLE2_ITEM = 3;

    private static UriMatcher uriMatcher;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.app.provider","table1",TABLE1_DIR);
        uriMatcher.addURI("com.example.app.provider","table1/#",TABLE1_ITEM);
        uriMatcher.addURI("com.example.app.provider","table2",TABLE2_DIR);
        uriMatcher.addURI("com.example.app.provider","table2/#",TABLE2_ITEM);
    }

    @Override
    public boolean onCreate()
    {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        switch (uriMatcher.match(uri))
        {
            case TABLE1_DIR:
                //查询table表中的所有数据
                break;
            case TABLE1_ITEM:
                //查询table1表中的单条数据
                break;
            case TABLE2_DIR:
                //查询table2表中的所有数据
                break;
            case TABLE2_ITEM:
                //查询table2表中的单条数据
                break;
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri)
    {
        switch (uriMatcher.match(uri))
        {
            case TABLE1_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.app.provider.table1";
            case TABLE1_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.app.provider.table1";
            case TABLE2_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.app.provider.table2"
            case TABLE2_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.app.provider.table2";
        }

        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        return 0;
    }

}
```



### 实现跨程序数据共享

右击com.example.databasetest包---New--Other---Content Provider

我们将内容提供器命名为`DatabaseProvider`,authority指定为`com.example.databasetest.provider`,Exported属性表示是否允许外部程序访问我们的内容提供器，Enabled属性表示是否启用这个内容提供器。

```dart
public class DatabaseProvider extends ContentProvider
{
    public static final int BOOK_DIR = 0;

    public static final int BOOK_ITEM = 1;

    public static final int Category_DIR = 2;

    public static final int Category_ITEM = 3;

    public static final String AUTHORITY ="com.example.databasetest.provider";

    private static UriMatcher uriMatcher;

    private MyDatabaseHelper dbHelper;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY,"book",BOOK_DIR);
        uriMatcher.addURI(AUTHORITY,"book/#",BOOK_ITEM);
        uriMatcher.addURI(AUTHORITY,"category",Category_DIR);
        uriMatcher.addURI(AUTHORITY,"category",Category_ITEM);
    }

    @Override
    public boolean onCreate()
    {
        dbHelper = new MyDatabaseHelper(getContext(),"BookStore.db",null,2);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder)
    {
        //查询数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri))
        {
            case BOOK_DIR:
                cursor = db.query("Book",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                cursor = db.query("Book",projection,"id = ?",new String[]{bookId},null,null,sortOrder);
                break;
            case Category_DIR:
                cursor = db.query("Category",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case Category_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                cursor = db.query("Category",projection,"id = ?",new String[]{categoryId},null,null,sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        //添加数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri))
        {
            case BOOK_DIR:
            case BOOK_ITEM:
                long newBookId = db.insert("Book",null,values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/book/" + newBookId);
                break;
            case Category_DIR:
            case Category_ITEM:
                long newCategory = db.insert("Category",null,values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/category/" + newCategory);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs)
    {
        //更新数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updatedRows = 0;
        switch (uriMatcher.match(uri))
        {
            case BOOK_DIR:
                updatedRows = db.update("Book",values,selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                updatedRows = db.update("Book",values,"id = ?",new String[]{bookId});
                break;
            case Category_DIR:
                updatedRows = db.update("Category",values,selection,selectionArgs);
                break;
            case Category_ITEM:
                String categoryId = uri.getPathSegments().get(1);
                updatedRows = db.update("Category",values,"id = ?",new String[]{categoryId});
                break;
            default:
                break;
        }
        return updatedRows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        //删除数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri))
        {
            case BOOK_DIR:
                deleteRows = db.delete("Book",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String bookId = uri.getPathSegments().get(1);
                deleteRows = db.delete("Book","id = ?",new String[]{bookId});
                break;
            case Category_DIR:
                deleteRows = db.delete("Category",selection,selectionArgs);
                break;
            case Category_ITEM:
                String category = uri.getPathSegments().get(1);
                deleteRows = db.delete("Category","id = ?",new String[]{category});
                break;
            default:
                break;
        }
        return deleteRows;
    }

    @Override
    public String getType(Uri uri)
    {
        switch (uriMatcher.match(uri))
        {
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.book";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.book";
            case Category_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.category";
            case Category_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.category";
        }
        return null;
    }

}
```

在类的一开始，同样是定义了4个常量，分别用于表示访问Book表中的所有数据，访问Book表中的单条数据，访问Category表中的所有数据和访问Category表中的单条数据。然后在静态代码块里对UriMatcher进行了初始化操作，将期望匹配的几种URI格式添加了进去。

接下来就是每个抽象方法的具体实现了，先来看下onCreate()方法，这个方法的代码很短，就是创建了一个MyDatabaseHelper的实例，然后返回true表示内容提供器初始化成功，这时数据库就已经完成了创建或升级操作。

query()方法，先获取到了SQLiteDatabase的实例，然后根据传入的Uri参数判断用户想要访问哪张表，再调用SQLiteDatabase的query()进行查询，并将Cursor对象返回就好了。**注意访问单条数据的时候有一个细节，这里调用了Uri对象的getPathSegments()方法，它会将内容URI权限之后的部分以"/"符号进行分割，并把分割后的结果放入到一个字符串列表中，那这个列表中第0个位置存放的就是路径，第一个位置存放的就是id了。得到了id 之后，再通过selection和selectionArgs参数进行约束，就实现了查询单条数据的功能**。

insert()方法，先获取到了SQLiteDatabase的实例，根据传入的Uri参数判断用户想要往哪张表里添加数据，再调用SQLiteDatabase的insert()进行添加，**注意insert()方法要求返回一个能够表示这条新增数据的URI，所以我们还需要调用Uri.parse()方法来将一个内容URI解析成Uri对象**                                                
 update()方法，先获取到了SQLiteDatabase的实例，然后根据传入的Uri参数判断用户想要更新哪张表的数据，再调用SQLiteDatabase的update()进行更新就好了，受影响的行数将作为返回值返回。

delete()方法，先获取到了SQLiteDatabase的实例，然后根据传入的Uri参数判断用户想要删除哪张表的数据，再调用SQLiteDatabase的delete()进行删除就好了，被删除的行数将作为返回值返回。

**另外还有一点需要注意，内容提供器一定要在AndroidManifest.xml文件中注册才可以使用**。

```xml
<provider
          android:name=".DatabaseProvider"
          android:authorities="com.example.databasetest.provider"
          android:enabled="true"
          android:exported="true">
</provider>
```



### 访问DatabaseTest中的数据

```java
public class MainActivity extends AppCompatActivity
{

    Button  button_add_data,button_query_data,
            button_update_data,button_delete_data;

    private String newId;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    public void initView()
    {
        button_add_data = (Button) findViewById(R.id.add_data);
        button_query_data = (Button) findViewById(R.id.query_data);
        button_update_data = (Button) findViewById(R.id.update_data);
        button_delete_data = (Button) findViewById(R.id.delete_data);
    }

    public void initEvent()
    {
        //添加数据
        button_add_data.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book");
                ContentValues values = new ContentValues();
                values.put("name","A Clash of Kings");
                values.put("author","Georgr Martin");
                values.put("pages",1040);
                values.put("price",22.85);
                Uri newUri = getContentResolver().insert(uri,values);
                newId = newUri.getPathSegments().get(1);
            }
        });

        //查看数据
        button_query_data.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book");
                Cursor cursor = getContentResolver().query(uri,null,null,null,null);
                if (cursor != null)
                {
                    while (cursor.moveToNext())
                    {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));

                        Log.d(TAG, "book name is :" + name);
                        Log.d(TAG, "book author is " + author);
                        Log.d(TAG, "book pages is :" + pages);
                        Log.d(TAG, "book price is :" + price);

                    }
                    cursor.close();
                }
            }
        });

        //更新数据
        button_update_data.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book/" + newId);
                ContentValues values = new ContentValues();
                values.put("name","A Storm of Swords");
                values.put("pages",1216);
                values.put("price",24.05);
                getContentResolver().update(uri,values,null,null);
            }
        });

        //删除数据
        button_delete_data.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book/" + newId);
                getContentResolver().delete(uri,null,null);
            }
        });
    }
}
```

