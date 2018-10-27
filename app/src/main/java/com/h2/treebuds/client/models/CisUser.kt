package com.h2.treebuds.client.models

object Model {
  data class CisUser(val users: List<User>)

  data class User(val displayName: String, val id: String)
}
