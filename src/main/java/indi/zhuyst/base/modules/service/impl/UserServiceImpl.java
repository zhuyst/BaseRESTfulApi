package indi.zhuyst.base.modules.service.impl;

import indi.zhuyst.base.common.exception.CommonException;
import indi.zhuyst.base.common.exception.FieldErrorException;
import indi.zhuyst.base.common.pojo.FieldError;
import indi.zhuyst.base.common.pojo.PageInfo;
import indi.zhuyst.base.common.pojo.Query;
import indi.zhuyst.base.common.service.impl.BaseCrudServiceImpl;
import indi.zhuyst.base.common.util.PageUtils;
import indi.zhuyst.base.modules.dao.UserDao;
import indi.zhuyst.base.modules.entity.UserDO;
import indi.zhuyst.base.modules.pojo.UserDTO;
import indi.zhuyst.base.modules.service.UserService;
import indi.zhuyst.base.modules.setting.DefaultAdminSettings;
import indi.zhuyst.base.security.enums.RoleEnum;
import indi.zhuyst.base.security.enums.StatusEnum;
import indi.zhuyst.base.security.pojo.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户服务实现类
 * @author zhuyst
 */
@Service
public class UserServiceImpl extends BaseCrudServiceImpl<UserDao,UserDO>
        implements UserService,CommandLineRunner{

    private static final String ADMIN_ID = "5aa67f46fda9b72cd0325e7a";

    private final DefaultAdminSettings defaultAdminSettings;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(DefaultAdminSettings defaultAdminSettings, PasswordEncoder passwordEncoder) {
        this.defaultAdminSettings = defaultAdminSettings;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 初始化管理员
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void run(String... strings) {
        UserDO user = super.getByID(ADMIN_ID);
        if(user == null){
            user = new UserDO();

            user.setId(ADMIN_ID);
            user.setUsername(defaultAdminSettings.getUsername());
            user.setPassword(passwordEncoder.encode(defaultAdminSettings.getPassword()));
            user.setNickname(defaultAdminSettings.getNickname());
            user.setRole(RoleEnum.SYS_ADMIN.getId());
            user.setStatus(StatusEnum.NORMAL.getId());

            Date date = new Date();
            user.setCreateDate(date);
            user.setModifiedDate(date);

            dao.insert(user);
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public UserDO save(UserDO user) {
        if(user.getId() == null){

            // 设置初始角色及状态
            user.setRole(RoleEnum.VISITOR.getId());
            user.setStatus(StatusEnum.NORMAL.getId());

            this.checkUserInfo(user,true,true);
        } else {
            UserDO oldUser = super.getByID(user.getId());

            // 保证username不被修改
            user.setUsername(oldUser.getUsername());

            boolean checkNickname = true;
            // 如果两者相等，则表示nickname不需要修改
            if(oldUser.getNickname().equals(user.getNickname())) {
                checkNickname = false;
            }

            // 保证角色及状态不被修改
            user.setRole(oldUser.getRole());
            user.setStatus(oldUser.getStatus());

            this.checkUserInfo(user,false,checkNickname);
        }

        String password = user.getPassword();
        if(password != null){
            password = passwordEncoder.encode(user.getPassword());
            user.setPassword(password);
        }

        return super.save(user);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(String id) {
        UserDTO user = this.getUserDTO(id);
        if(user.getAdmin()){
            throw new CommonException("管理员不能被删除");
        }

        super.delete(id);
    }

    @Override
    public UserDTO getUserDTO(String id){
        UserDO user = super.getByID(id);
        return this.produceDTO(user);
    }

    @Override
    public UserDO getByUsername(String username) {
        UserDO queryUser = new UserDO();
        queryUser.setUsername(username);

        Example<UserDO> example = Example.of(queryUser);
        return dao.findOne(example);
    }

    @Override
    public UserDO getByNickName(String nickname){
        UserDO queryUser = new UserDO();
        queryUser.setNickname(nickname);

        Example<UserDO> example = Example.of(queryUser);
        return dao.findOne(example);
    }

    @Override
    public PageInfo<UserDTO> listUserDTO(Query<UserDO> query) {
        PageInfo<UserDO> pageInfo = listByCondition(query);
        List<UserDTO> list = new ArrayList<>();
        for(UserDO user : pageInfo.getList()){
            list.add(produceDTO(user));
        }
        return PageUtils.copyNewPageInfo(pageInfo,list);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null){
            throw new UsernameNotFoundException("用户名/密码错误");
        }

        UserDO user = this.getByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("用户名/密码错误");
        }

        return new SecurityUser(user);
    }

    /**
     * 检查{@link UserDO#username}和{@link UserDO#nickname}是否存在重复
     * 如果存在重复则会抛出{@link FieldErrorException}
     * @param user 检查的用户对象
     * @param checkUsername 是否检查{@link UserDO#username}
     * @param checkNickname 是否检查{@link UserDO#nickname}
     */
    private void checkUserInfo(UserDO user,boolean checkUsername,boolean checkNickname){
        final String fieldUsername = "username";
        final String fieldNickname = "nickname";

        UserDO oldUser;
        List<FieldError> errors = new ArrayList<>(2);
        if(checkUsername){
            oldUser = this.getByUsername(user.getUsername());
            if(oldUser != null){
                FieldError error = new FieldError(fieldUsername,"该用户名已被使用，请换一个用户名试试");
                errors.add(error);
            }
        }

        if(checkNickname){
            oldUser = this.getByNickName(user.getNickname());
            if(oldUser != null){
                FieldError error = new FieldError(fieldNickname,"该昵称已被使用，请换一个昵称试试");
                errors.add(error);
            }
        }

        if(!errors.isEmpty()){
            throw new FieldErrorException(errors);
        }
    }

    /**
     * 将DO封装为DTO
     * @param user DO
     * @return DTO
     */
    private UserDTO produceDTO(UserDO user){
        if(user == null){
            return null;
        }

        user.setPassword(null);
        return new UserDTO(user);
    }
}
