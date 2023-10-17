package com.example.blogsystemtest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blogsystemtest.domain.ResponseResult;
import com.example.blogsystemtest.domain.dto.AddUserDto;
import com.example.blogsystemtest.domain.dto.ChangeUserDto;
import com.example.blogsystemtest.domain.dto.UpdateUserDto;
import com.example.blogsystemtest.domain.dto.UserListDto;
import com.example.blogsystemtest.domain.entity.Role;
import com.example.blogsystemtest.domain.entity.User;
import com.example.blogsystemtest.domain.entity.UserRole;
import com.example.blogsystemtest.domain.vo.PageVo;
import com.example.blogsystemtest.domain.vo.UserInfoVo;
import com.example.blogsystemtest.domain.vo.UserVo;
import com.example.blogsystemtest.enums.AppHttpCodeEnum;
import com.example.blogsystemtest.exception.SystemException;
import com.example.blogsystemtest.mapper.RoleMapper;
import com.example.blogsystemtest.mapper.UserMapper;
import com.example.blogsystemtest.service.UserRoleService;
import com.example.blogsystemtest.service.UserService;
import com.example.blogsystemtest.utils.BeanCopyUtils;
import com.example.blogsystemtest.utils.RedisCache;
import com.example.blogsystemtest.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserVo vo = BeanCopyUtils.copyBean(user,UserVo.class);

        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(UpdateUserDto updateUserDto) {
        User user=BeanCopyUtils.copyBean(updateUserDto,User.class);
        updateById(user);
        redisTemplate.opsForValue().set("blog:login:"+updateUserDto.getId(),user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public ResponseResult register(User user) {
        user.setType("0");
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getType())){
            throw new SystemException(AppHttpCodeEnum.TYPE_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if (userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (userNickExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (userEmailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName,user.getUserName());
        Long id=userMapper.selectOne(lambdaQueryWrapper).getId();

        //获取权限字符串对应的roleid
        LambdaQueryWrapper<Role> lambdaQueryWrapper1=new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Role::getRoleKey,user.getType());
        Long roleid=roleMapper.selectOne(lambdaQueryWrapper1).getId();
        //存入userrole关系表
        userRoleService.save(new UserRole(id,roleid));

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo> pageUserList(Integer pageNum, Integer pageSize, UserListDto userListDto) {
        //分页查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if(Objects.nonNull(userListDto)){
            //模糊查询username
            queryWrapper.like(StringUtils.hasText(userListDto.getUserName()),User::getUserName,userListDto.getUserName());
            //模糊查询phonenumber
            queryWrapper.like(StringUtils.hasText(userListDto.getPhonenumber()),User::getPhonenumber,userListDto.getPhonenumber());
            //查询状态
            queryWrapper.eq(StringUtils.hasText(userListDto.getStatus()),User::getStatus,userListDto.getStatus());
            //模糊查询username
            queryWrapper.like(StringUtils.hasText(userListDto.getUserName()),User::getUserName,userListDto.getUserName());
            //模糊查询phonenumber
            queryWrapper.like(StringUtils.hasText(userListDto.getPhonenumber()),User::getPhonenumber,userListDto.getPhonenumber());
            //查询状态
            queryWrapper.eq(StringUtils.hasText(userListDto.getStatus()),User::getStatus,userListDto.getStatus());
        }
        Page<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        Page<User> userList = page(page, queryWrapper);
        //封装数据返回
        //封装成tagVo集合
        List<UserVo> userVos = BeanCopyUtils.copyBeanList(userList.getRecords(), UserVo.class);
        PageVo  pageVo = new PageVo(userVos,userList.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        //对数据进行非空判断
        if (!StringUtils.hasText(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(addUserDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if (userNameExist(addUserDto.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (userNickExist(addUserDto.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (userEmailExist(addUserDto.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        save(user);

        List<Long> roleIds = addUserDto.getRoleIds();
        List<UserRole> userRoles = roleIds.stream()
                .map(roleId -> new UserRole(user.getId(), roleId))
                .collect(Collectors.toList());

        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Transactional
    @Override
    public ResponseResult deleteUser(Long id) {
        removeById(id);
        userRoleService.removeById(id);
        redisTemplate.delete("blog:login:"+id);
        return ResponseResult.okResult();
    }

    @Override
    public User getUser(Long id) {
        User reUser = getById(id);
        return reUser;
    }

    @Transactional
    @Override
    public ResponseResult updateUser(UpdateUserDto updateUserDto) {
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        updateById(user);
        redisTemplate.opsForValue().set("blog:login:"+updateUserDto.getId(),user);

        //最新角色集合
        List<Long> roleIds = updateUserDto.getRoleIds();
        //更新前的角色集合
        List<Long> reRoleIds = userRoleService.getRoleIds(updateUserDto.getId());

        List<UserRole> userRoleList = roleIds.stream()
                .filter(roleId -> !reRoleIds.contains(roleId))
                .map(roleId -> new UserRole(updateUserDto.getId(), roleId))
                .collect(Collectors.toList());

        if (userRoleList.size()>0){
            userRoleService.saveBatch(userRoleList);
        } else if (userRoleList.size()==0) {
            List<UserRole> userRoleList2 = reRoleIds.stream()
                    .filter(reRoleId -> !roleIds.contains(reRoleId))
                    .map(reRoleId -> new UserRole(updateUserDto.getId(), reRoleId))
                    .collect(Collectors.toList());

            for (UserRole userRole : userRoleList2) {
                LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(UserRole::getUserId,userRole.getUserId());
                queryWrapper.eq(UserRole::getRoleId,userRole.getRoleId());
                userRoleService.remove(queryWrapper);
            }
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult changeStatus(ChangeUserDto changeUserDto) {
        User user = getBaseMapper().selectById(changeUserDto.getUserId());
        user.setStatus(changeUserDto.getStatus());
        updateById(user);
        redisTemplate.opsForValue().set("blog:login:"+changeUserDto.getUserId(),user);
        return ResponseResult.okResult();
    }

    private boolean userEmailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(User::getEmail,email);
        return count(queryWrapper)>0;
    }

    private boolean userNickExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(User::getNickName,nickName);
        return count(queryWrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper)>0;
    }
}
