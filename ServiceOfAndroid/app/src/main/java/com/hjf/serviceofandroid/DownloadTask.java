package com.hjf.serviceofandroid;
import android.os.AsyncTask;

/**
 * @author hjf
 * @create 2020-12-18 20:29
 */
public class DownloadTask extends AsyncTask<Void, Integer, Boolean> {

    /**
     * 这个方法会在后台任务开始执行之前调用，用于进行一些界面上的初始化操作
     */
    @Override
    protected void onPreExecute() {
    }

    /**
     * 当后台任务执行完毕并通过 return 语句进行返回时，这个方法就很快会被调用。
     * 返回的数据会作为参数传递到此方法中，可以利用返回的数据来进行一些 UI 操作
     * @param aBoolean
     */
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    /**
     * 当在后台任务中调用了publishProgress(Progress…)方法后，这个方法就会很快被调用，
     * 方法中携带的参数就是在后台任务中传递过来的。在这个方法中可以对 UI 进行操作，
     * 利用参数中的数值就可以对界面元素进行相应地更新
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    /**
     * 这个方法中的所有代码都会在子线程中运行，我们应该在这里去处理所有的耗时任务。
     * 任务一旦完成就可以通过 return 语句来将任务的执行结果返回，在此方法中不能进行UI操作
     * @param voids
     * @return
     */
    @Override
    protected Boolean doInBackground(Void... voids) {

        // 抛出任务执行的进度给 onProgressUpdate(Integer... values)方法
        publishProgress(1);
        return null;
    }
}
