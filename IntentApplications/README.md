# Intent的用法

## 使用显式 Intent

```xml
# app/src/main/res/layout/second_layout.xml

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <Button
        android:id="@+id/button_2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Button 2"
        />

</LinearLayout>
```

```java
# app/src/main/java/com/hjf/activitytest/SecondActivity.java

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);
    }
}
```

```xml
# app/src/main/AndroidManifest.xml

<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.hjf.activitytest">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">
        <activity
            android:name=".FirstActivity"                              #注册的活动名称，可省略包名
            android:label="This is FirstActivity">                     #指定活动标题
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />   #设为主活动
                <category android:name="android.intent.category.LAUNCHER" /> #设为启动器
            </intent-filter>
        </activity>
        <activity
            android:name=".SecondActivity">                            #注册第二个活动
        </activity>
    </application>

</manifest>
```

```java
public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        Button button1 = (Button) findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 实例化 Intent，第一个参数为当前上下文，第二个参数为要启动的目标活动
                Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                // 启动目标活动
                startActivity(intent);
            }
        });
    }
}
```



## 使用隐式 Intent

```xml
<activity
    android:name=".SecondActivity">
    <intent-filter>
        # 指定当前活动相应的行为是 ACTION_START
        <action android:name="com.hjf.activitytest.ACTION_START" />
        # 指定当前活动相应的分类是 DEFAULT
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```

```java
public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        
        Button button1 = (Button) findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 实例化 Intent，并设置行为是 ACTION_START，不设置分类则默认分类是 DEFAULT
                Intent intent = new Intent("com.hjf.activitytest.ACTION_START");
                startActivity(intent);
            }
        });
    }
}
```

增加活动分类：

```xml
<activity
    android:name=".SecondActivity">
    <intent-filter>
        # 指定当前活动相应的行为是 ACTION_START
        <action android:name="com.hjf.activitytest.ACTION_START" />
        # 指定当前活动相应的分类是 DEFAULT
        <category android:name="android.intent.category.DEFAULT" />
        # 指定当前活动相应的分类是 MY_CATEGORY
        <category android:name="com.hjf.activitytest.MY_CATEGORY" />
    </intent-filter>
</activity>
```

```java
public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        
        Button button1 = (Button) findViewById(R.id.button_1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 实例化 Intent，并设置行为是 ACTION_START
                Intent intent = new Intent("com.hjf.activitytest.ACTION_START");
                // 为 Intent 设置分类是 MY_CATEGORY
                intent.addCategory("com.hjf.activitytest.MY_CATEGORY");
                startActivity(intent);
            }
        });
    }
}
```



## 更多隐式 Intent 的用法

```java
button1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // 实例化 Intent，并设置行为是 ACTION_VIEW
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 设置 Intent 的 Uri
        intent.setData(Uri.parse("https://www.baidu.com"));
        // 启动活动，会打开系统浏览器访问设置的网址
        startActivity(intent);
    }
});
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <Button
        android:id="@+id/button_3"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Button 3"
        />

</LinearLayout>
```

```xml
<activity android:name=".ThirdActivity">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.BROWSABLE" />
        # 指定数据的协议部分是 http
        <data android:scheme="http" />
    </intent-filter>
</activity>
```

调用系统拨号界面：

```java
button1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:10086"));
        startActivity(intent);
    }
});
```



## 向下一个活动传递数据

```java
button1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String data = "Hello SecondActivity";
        Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
        // 暂存数据到 Intent 中
        intent.putExtra("extra_data", data);
        startActivity(intent);
    }
});
```

```java
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);
        Intent intent = getIntent();
        // 从 Intent 中取出数据
        String data = intent.getStringExtra("extra_data");
        Log.d("SecondActivity", data);
    }
}
```



## 返回数据给上一个活动

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.first_layout);
    Button button1 = (Button) findViewById(R.id.button_1);
    button1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
            // 启动活动，并接受活动销毁时返回的数据，第二个参数为请求码，用于判断数据的来源
            startActivityForResult(intent, 1);
        }
    });
}

// 下一个活动被销毁时的回调函数
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // 判断数据来源
    switch (requestCode) {
        case 1:
            if (resultCode == RESULT_OK) {
                String returnedData = data.getStringExtra("data_return");
                Log.d("FirstActivity", returnedData);
            }
            break;
        default:
    }
}
```

```java
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SecondActivity", "Task id is " + getTaskId());
        setContentView(R.layout.second_layout);
        Button button2 = (Button) findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                // 暂存数据到 Intent 中
                intent.putExtra("data_return", "Hello FirstActivity");
                // 向上一个活动返回数据，第一个参数一般为 RESULT_OK
                setResult(RESULT_OK, intent);
                // 销毁当前活动
                finish();
            }
        });
    }

    // 绑定按下返回键时的逻辑，与按钮点击的逻辑一致
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("data_return", "Hello FirstActivity");
        setResult(RESULT_OK, intent);
        finish();
    }
}
```

