package study.msa.msauserservice.user.jpa

import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<UserEntity, Long>
