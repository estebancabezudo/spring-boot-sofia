package users.mappers;

import net.cabezudo.sofia.core.persistence.EntityList;
import users.persistence.UserEntity;
import users.service.UserList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityToBusinessUserListMapper {

  private @Autowired EntityToBusinessUserMapper entityToBusinessUserMapper;

  public UserList map(EntityList<UserEntity> entityList) {
    int total = entityList.getTotal();
    int start = entityList.getStart();
    int size = entityList.size();
    UserList list = new UserList(total, start, size);

    for (UserEntity userEntity : entityList) {
      list.add(entityToBusinessUserMapper.map(userEntity));
    }
    return list;
  }
}
