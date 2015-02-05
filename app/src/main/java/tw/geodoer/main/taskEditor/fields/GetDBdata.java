package tw.geodoer.main.taskEditor.fields;

import tw.geodoer.mDatabase.contentProvider.TaskDbProvider;

/**
 * Created by Kent on 2014/12/24.
 */
public class GetDBdata {

    private TaskDbProvider taskDbProvider = new TaskDbProvider();

    public GetDBdata() {
    }

    public TaskDbProvider getTaskDbProvider() {
        return taskDbProvider;
    }

}
