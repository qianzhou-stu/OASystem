package com.andreas.oa.serviceImpl;

import com.andreas.oa.pojo.Node;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：MainServiceImpl测试类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application.xml","classpath:spring-mvc.xml"})
public class MainServiceImplTest extends TestCase {
    @Resource
    private MainServiceImpl mainServiceImpl = new MainServiceImpl();
    @Test
    public void selectNodeByUserId(){
        List<Node> nodes = mainServiceImpl.selectNode(1L);
        System.out.println(nodes);
    }
}