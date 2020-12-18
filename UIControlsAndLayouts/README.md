# UI常用控件和布局

## 常用控件

### TextView

Android中最常用的控件之一

```xml
<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="This is a TextView"
    android:gravity="center"
    />
```

android:gravity来指定文字的对齐方式，可选值有top、bottom、left、right、center等，可以用“|”来同时指定多个值。

![img](README.assets/20180327101733109)

使用textSize来指定文字的大小，字体大小用sp作为单位。

textColor指定文字的颜色。

```xml
<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="This is a TextView"
    android:gravity="center"
    android:textSize="24sp"
    android:textColor="#00ff00"
    />
```

![img](README.assets/20180327101817202)



### Button

button是程序用于和用户进行交互的一个重要控件。

```xml
<Button
    android:id="@+id/button"
    android:text="Button"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

![img](README.assets/20180327102148440)

button会将英文字母自动进行大小写转换，可以使用以下配置来禁用这一默认特性：

```xml
<Button
    android:id="@+id/button"
    android:text="Button"
    android:textAllCaps="false"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

![img](README.assets/20180327102306800)

为button注册事件监听，有两种方式

1.

```java
Button button=findViewById(R.id.button);
button.setOnClickListener(new View.OnClickListener() {
  @Override
  public void onClick(View view) {


  }
});
```

2.

```java
public class MainActivity extends BaseActivity implements View.OnClickListener {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity",this.toString());
        Button button=findViewById(R.id.button);
        button.setOnClickListener(this);
 
    }
 
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
        }
    }
}
```



### EditText

程序用于和用户进行交互的一个重要控件，它允许用户在控件里输入和编辑内容，并可以在程序中对这些内容进行处理。

```html
<EditText
    android:id="@+id/edit_text"
    android:hint="Type something here"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```



![img](README.assets/20180327102723104)

hint属性指定了一段提示性的文本。

不过，随着输入的内容不断增多，EditText会被不断地拉长。我们可以使用maxLines属性来解决这个问题



### ImageView

用于显示图片的一个控件，图片通常放在以“drawable”开头的目录下。



### ProgressBar

用于在界面上显示一个进度条，表示我们的程序正在加载一些数据。它的用法也非常简单。

在加载完成后，可以通过visibility进行指定，可选值有三种：visible、invisible和gone。

visible表示控件是可见的，invisible表示控件不可见，但是它仍占据着原来的位置和大小。gone则表示控件不仅不可见，而且不再占用屏幕控件。可以通过代码来设置控件的可见性，使用的是setVisibility（）方法，可以传入View.VISIBLE、View.INVISIBLE和View.GONE这三种值。

```java
 @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                if(progressBar.getVisibility()==View.GONE){
                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    progressBar.setVisibility(View.GONE);
                }
                break;
        }
    }
```

设置成为水平进度条

```html
<ProgressBar
    android:id="@+id/progress_bar"
    style="?android:attr/progressBarStyleHorizontal"
    android:max="100"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" />
```

```java
@Override
public void onClick(View view) {
  switch (view.getId()){
    case R.id.button:
      int progress=progressBar.getProgress();
      progress=progress+10;
      if(progress<=100){
        progressBar.setProgress(progress);
      }
      break;
  }
}
```

![img](README.assets/20180327105822260)



### AlertDialog

可以在当前的界面弹出一个对话框，这个对话框式置顶于所有界面元素之上的，能够屏蔽掉其他控件的交互能力，因此AlertDialog一般都是用于提示一些非常重要的内容或者警告信息。

```java
public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("This is dialog");
                dialog.setMessage("Something important");
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
 
                    }
                });
                dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
 
                    }
                });
                dialog.show();
                break;
        }
    }
}
```

![img](README.assets/20180414195651706)



### ProgressDialog

ProgressDialog和AlertDialog有点类似，都可以在界面上弹出一个对话框，都能够屏蔽掉其他控件的交互能力。不同的是，ProgressDialog会在对话框中显示一个进度条，一般用于表示当前操作比较耗时，让用户耐心地等待。

```java
public void onClick(View view) {
  switch (view.getId()){
    case R.id.button:
      ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
      progressDialog.setTitle("This is ProgressDialog");
      progressDialog.setMessage("Loading...");
      progressDialog.setCancelable(true);
      progressDialog.show();
      break;
  }
}
```

![img](README.assets/20180414200218117)



------



## 四种基本布局

### 线性布局

**LinearLayout**又称线性布局，是一种非常常用的布局

**android:orientation**属性用来指定线性布局的排列方式，有2个可选值：**horizontal** 和 **vertical** ，分别表示水平排列和垂直排列，若不指定，则默认是**horizontal**

下面我们来实践一下，修改activity_main.xml中的代码，如下所示

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"
    android:orientation="horizontal">

    <Button
        android:id="@+id/button1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Button 1"/>

    <Button
        android:id="@+id/button2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Button 2"/>

    <Button
        android:id="@+id/button3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Button 2"/>

</LinearLayout>
```

需要注意的是如果 **LinearLayout** 的排列方式是**horizontal**，则内部控件的宽度**layout_width**就不能指定为**match_parent**，因为这样的话控件会占满屏幕，其它控件就没有位置可以放下了。同样的，如果排列方式是**vertical**，则内部控件的高度 **layout_height** 不能指定为**match_parent**

运行效果如下图所示

![img](README.assets/15449003-cfdde3de49b6797e)

下面来学习**android:layout_gravity**属性，这个跟**android:gravity**很相似，它们的区别是：**android:gravity**是指文本在控件中的对齐方式，**android:layout_gravity**是控件在布局中的对齐方式。

**android:layout_gravity**的可选值和**android:gravity**差不多，但是需要注意的是**LinearLayout**排列方式是**horizontal**时，只有垂直方向上的对齐方式才会生效，因为此时水平方向上的长度是不固定的，每添加或减少一个控件，水平方向上的长度都会改变，因而无法指定水平方向上的对齐方式。同理，当**LinearLayout**的排列方式**vertical**时，只有水平方向上的对齐方式才会有效

修改activity_main.xml中的代码，如下所示

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"
    android:orientation="horizontal">

    <Button
        android:id="@+id/button1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="top"
        android:text="Button 1"/>

    <Button
        android:id="@+id/button2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_vertical"
        android:text="Button 2"/>

    <Button
        android:id="@+id/button3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom"
        android:text="Button 2"/>

</LinearLayout>
```

因为此时LinearLayout的排列方向是**horizontal**，所以里面的Button控件只能指定垂直方向上的排列方向，重新运行程序，效果如下所示

![img](README.assets/15449003-dc7c698fd4373a6d)

接下来我们来学习**android:layout_weight**，这个属性允许我们以比例的方式来指定控件的大小，在手机屏幕的适配方面起到了非常重要的作用。

修改activity_main.xml中的代码，如下所示

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"
    android:orientation="horizontal">

    <EditText
        android:id="@+id/input_message"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        />

    <Button
        android:id="@+id/send"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="Send"/>

</LinearLayout>
```

这里**android:layout_width**都指定成了0dp,那么文本编辑框和按钮还能显示出来吗？答案是肯定的，因为我们这里使用了**android:layout_weight**属性，这时的控件宽度将不再由**android:layout_width**决定，这里指定成**android:layout_width="0dp"** 是一种比较规范的写法。另外dp是Android中用来作为控件大小、间距等属性的单位。

我们这里EditText和Button元素中的 **android:layout_weight** 都指定为1，意思是EditText和Button平分屏幕的水平宽度。

控件所占的比例是这么算出来的：系统会将LinearLayout下的所有控件的 **android:layout_weight** 值相加得到一个总值，每个控件所占的比例就是该控件的**android:layout_weight**值除以这个总值得到的

我们还可以通过指定部分控件的**android:layout_weight**值来实现更好的效果，修改activity_main.xml中代码，如下所示

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"
    android:orientation="horizontal">

    <EditText
        android:id="@+id/input_message"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:hint="Type something"
        />

    <Button
        android:id="@+id/send"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Send"/>
    
</LinearLayout>
```

这里仅指定了EditText的**android:layout_weight**属性，Button的**android:layout_width**改为了**wrap_content**，这时Button的宽度仍然是按照**wrap_content**来计算，而EditText则会占满屏幕所剩余的空间。这种编程方式，不仅在各种屏幕的适配方面非常好，而且看起来也更加舒服。

重新运行程序，效果如下所示

![img](README.assets/15449003-01d58657bef30cfe)



### 相对布局

**RelativeLayout**又称相对布局，也是一种非常常用的布局。

我们直接来体验一下，修改activity_main.xml中的代码，如下所示

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/button1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentLeft="true"
        android:layout_alignParentTop="true"
        android:text="Button1"/>

    <Button
        android:id="@+id/button2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentRight="true"
        android:layout_alignParentTop="true"
        android:text="Button2"/>

    <Button
        android:id="@+id/button3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:text="Button3"/>

    <Button
        android:id="@+id/button4"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentLeft="true"
        android:layout_alignParentBottom="true"
        android:text="Button4"/>

    <Button
        android:id="@+id/button5"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentRight="true"
        android:layout_alignParentBottom="true"
        android:text="Button5"/>

</RelativeLayout>
```

以上代码不需要过多的解释，因为它们实在太好理解了。**android:layout_alignParentLeft**、**android:layout_alignParentTop**、**android:layout_alignParentRight**、**android:layout_centerInParent**、**android:layout_alignParentBottom**这几个属性我们之前没有接触过，但是它们的名字已经完全说明了它们的作用。

重新运行程序，效果如图所示

![img](README.assets/15449003-6c77c6920526c698)

上面的例子是控件相对于布局定位的，那么控件能否相对于控件进行定位呢？当然是可以的。下面我们就来实践一下，修改activity_main.xml中的代码，如下所示

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/button3"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:text="Button3"/>

    <Button
        android:id="@+id/button1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_above="@id/button3"
        android:layout_toLeftOf="@id/button3"
        android:text="Button1"/>

    <Button
        android:id="@+id/button2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_above="@id/button3"
        android:layout_toRightOf="@id/button3"
        android:text="Button2"/>
    <Button
        android:id="@+id/button4"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/button3"
        android:layout_toLeftOf="@id/button3"
        android:text="Button4"/>

    <Button
        android:id="@+id/button5"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_below="@id/button3"
        android:layout_toRightOf="@id/button3"
        android:text="Button5"/>
    
</RelativeLayout>
```

**android:layout_above**属性可以让一个控件位于另一个控件的上方，需要为这个属性指定相对控件id的引用，这里我们填入@id/button3，表示该控件位于Button3的上方。**android:layout_below**属性则是让一个控件位于另一个控件的下方。**android:layout_toLeftOf** 和 **android:layout_toRightOf** 则分别表示让一个控件位于另一个控件的左侧和右侧

当一个控件去引用另一个控件的id时，该控件一定要定义在引用控件的后面，否则会出现找不到id的情况。重新运行程序，效果如图

![img](README.assets/15449003-c90ad7336ae811bc)



### 帧布局

FrameLayout又称作帧布局，它比前面两种布局就简单太多了，应用场景也比较少。没有方便的定位方式，所有的控件默认放在布局的左上角。

下面我们通过例子来看看吧，修改activity_main.xml中的代码，如下所示

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

<TextView
    android:id="@+id/text_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="This is TextView"
    />

    <ImageView
        android:id="@+id/image_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@mipmap/ic_launcher"
        />
    
</FrameLayout>
```

FrameLayout中只放置了TextView和ImageView，需要注意的是，当前项目中我们并没有准备任何图片，所以这里ImageView直接使用了@mipmap来访问ic_launcher这张图，虽然这种用法的场景很少，但是却是完全可行的。重新运行程序，效果如图所示

可以看到，文字和图片都是位于布局的左上角，因为ImageView是在TextView之后添加的，所以图片压在了文字上面。我们可以通过 **android:layout_gravity** 来指定控件在布局中的对齐方式

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

<TextView
    android:id="@+id/text_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="This is TextView"
    android:layout_gravity="left"
    />

    <ImageView
        android:id="@+id/image_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@mipmap/ic_launcher"
        android:layout_gravity="right"
        />
    
</FrameLayout>
```

我们指定了TextView左对齐，ImageView右对齐。重新运行程序，效果如下图所示

![img](README.assets/15449003-bd21a4338073e054)



### 百分比布局

百分比布局可以直接指定控件在布局所占的百分比。由于LinearLayout本身已经支持按比例指定控件的大小了，因此百分比布局只对FrameLayout和RelativeLayout这两个布局进行了功能扩展，提供了 **PercentFrameLayout** 和 **PercentRelativeLayout** 这两个全新的布局

下面我们直接来实践一下吧，因为百分比布局属于新增布局，所以我们需要在项目的build.gradle中添加百分比布局库的依赖。

打开app/build.gradle文件，在dependencies闭包中添加如下内容：

```bash
implementation 'androidx.percentlayout:percentlayout:1.0.0'
```

需要注意的是，每当修改了任何的gradle文件，Android Studio都会有如下图所示的提示

![img](README.assets/15449003-cce3c44d9a22c3fa)

这个提示告诉我们，gradle自上次同步以后又发生了变化，需要再次同步才能让项目正常工作。这里只需要点击Sync Now就可以了，然后gradle会开始同步，把我们新添加的百分比布局库引入到项目当中。

接下来修改activity_main.xml中的代码，如下所示

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.percentlayout.widget.PercentFrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

  <Button
      android:id="@+id/button1"
      android:text="Button 1"
      android:layout_gravity="left|top"
      app:layout_widthPercent="50%"
      app:layout_heightPercent="50%"
      />

    <Button
        android:id="@+id/button2"
        android:text="Button 2"
        android:layout_gravity="right|top"
        app:layout_widthPercent="50%"
        app:layout_heightPercent="50%"
        />

    <Button
        android:id="@+id/button3"
        android:text="Button 3"
        android:layout_gravity="left|bottom"
        app:layout_widthPercent="50%"
        app:layout_heightPercent="50%"
        />

    <Button
        android:id="@+id/button4"
        android:text="Button 4"
        android:layout_gravity="right|bottom"
        app:layout_widthPercent="50%"
        app:layout_heightPercent="50%"
        />
</androidx.percentlayout.widget.PercentFrameLayout>
```

最外层我们使用了**PercentFrameLayout**，由于百分比布局并不是内置在系统SDK当中的，所示需要把完整的包路径写出来。然后必须定义一个app的命名空间，这样我们才能使用百分比布局的自定义属性。

使用 **app:layout_widthPercent** 属性将各按钮的宽度指定为布局的50%，使用 **app:layout_heightPercent** 属性将各按钮的高度指定为布局的50%。这里之所以能使用app前缀是因为我们刚才定义了app的命名空间，这跟我们一直能使用android前缀是一样的道理。

不过PercentFrameLayout还是继承了FrameLayout的特性，即所有的控件默认摆放在布局的左上角，为了让控件不重叠。我们这里借助了android:layout_gravity属性来分别把这4个按钮放在了左上，右上，左下，右下4个位置。

重新运行程序，效果如下图所示

![img](README.assets/15449003-8f09c6cf97a38119)

