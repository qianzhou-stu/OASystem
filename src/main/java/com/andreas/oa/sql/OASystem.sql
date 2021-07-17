select distinct n.*
from sys_role_user ru, sys_role_node rn,sys_node n
where ru.role_id = rn.role_id and user_id = 1 and rn.role_id = n.node_id
order by n.node_code;