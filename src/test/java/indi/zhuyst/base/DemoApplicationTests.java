package indi.zhuyst.base;

import indi.zhuyst.base.modules.entity.UserDO;
import indi.zhuyst.base.modules.service.UserService;
import indi.zhuyst.base.modules.entity.UserDO;
import indi.zhuyst.base.modules.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private UserService userService;

	@Test
	public void contextLoads() {
		for(int i = 1;i <= 1000;i++){
			UserDO user = new UserDO();
			user.setUsername("username" + i);
			user.setNickname("昵称" + i);
			userService.save(user);
		}
	}

}
