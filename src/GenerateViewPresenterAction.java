import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.concurrent.CancellationException;

/**
 * Created by Administrator on 2016/8/18.
 */
public class GenerateViewPresenterAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        final Project project = event.getData(CommonDataKeys.PROJECT);
        try {
            if (project == null) {
                throw new CancellationException("Unable to retrieve project");
            }
            VirtualFile selectedLayoutFile = getSelectedLayoutFile(event);
            Module moduleOfFile = getModuleOfFile(project, selectedLayoutFile);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void update(AnActionEvent e) {
        Project data = e.getData(CommonDataKeys.PROJECT);
        boolean visible = false;
        if(null != data) {
            VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
            if(null != file) {
                String path = file.getPath();
                System.out.println(path);
                visible = path.contains("res/layout") && path.endsWith(".xml");
            }
        }
        e.getPresentation().setVisible(visible);
    }

    private VirtualFile getSelectedLayoutFile(AnActionEvent event) {
        VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if(null != file) {
            return file;
        } else {
            throw new CancellationException("Please selected layout file");
        }
    }

    private Module getModuleOfFile(Project project, VirtualFile file) {
        ProjectRootManager manager = ProjectRootManager.getInstance(project);
        Module moduleForFile = manager.getFileIndex().getModuleForFile(file);
        if(null == moduleForFile) {
            throw new CancellationException("\"Failed to determine module with selected layout");
        }
        return moduleForFile;
    }
}
