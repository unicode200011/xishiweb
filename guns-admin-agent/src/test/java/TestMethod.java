import com.stylefeng.guns.admin.GunsAgentApplication;
import com.stylefeng.guns.persistence.dao.DeptMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GunsAgentApplication.class)
public class TestMethod {

    @Autowired
    DeptMapper deptMapper;

    @Test
    public void addJob() throws RuntimeException{

//        quartzService.addJob(quartzJobInfo);
    }

}
