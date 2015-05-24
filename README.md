# Tag_frontend
Tag前端

2015.3.15 更新说明
---
1. 右上角加号、底部导航栏统一放到EntryButtonActivity中。
2. EntryButtonActivity已继承FragmentActivity，使用时直接继承EntryButtonActivity即可。
3. EntryButtonActivity类中包含一个抽象方法getUrl，需要实现，该方法的返回值是用于生成二维码的字符串。在某标签页面点“加号”-“生成二维码”时调用。如果该页面非标签页面或者由于各种原因无法得到结果，返回null。
4. 右上角的加号的id务必定义为entry_button
5. 在OnCreate方法中，需要调用iniFooter()和iniPopupData()方法。
6. 关于底部导航栏，将footer.xml include进去即可。即，在xml放导航栏的位置插入如下代码：

在Relative Layout中：

    <include 
    layout="@layout/footer"
    android:id="@+id/bottom_choice"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_alignParentBottom="true"
    android:orientation="horizontal"/>

在LinearLayout中：

    <Space
    android:layout_width="wrap_content"
    android:layout_height="match_parent"
    android:layout_weight="1"/>
    <include layout="@layout/footer"/>

- LoginActivity和TagActivity已采用该方法实现
