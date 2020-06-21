import com.stylefeng.guns.admin.GunsApplication;
import com.stylefeng.guns.persistence.dao.DeptMapper;
import com.stylefeng.guns.persistence.model.Dept;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GunsApplication.class)
public class TestMethod {

    @Autowired
    DeptMapper deptMapper;

    @Test
    public void addJob() throws RuntimeException{

//        quartzService.addJob(quartzJobInfo);
    }

}
