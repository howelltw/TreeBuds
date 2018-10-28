package com.h2.treebuds.client.models

data class CisUser(
  val authentication: Any,
  val session: Any,
  val users: List<User>
)

data class User(
  val accountType: String,
  val alternateEmail: Any,
  val birthDate: String,
  val contactName: String,
  val country: String,
  val displayMembershipNumber: String,
  val displayName: String,
  val email: String,
  val familyName: String,
  val gender: String,
  val givenName: String,
  val helperPin: String,
  val id: String,
  val imageUrl: Any,
  val membershipNumber: String,
  val multiFactor: String,
  val oldPassword: Any,
  val parentalConsent: Any,
  val password: Any,
  val positions: List<Position>,
  val preferences: Any,
  val preferredLanguage: String,
  val sms: Sms,
  val type: String,
  val unVerified: UnVerified,
  val updateDate: String,
  val username: String,
  val ward: String,
  val workForce: String
)

data class UnVerified(
  val altEmailAddress: Any,
  val emailAddress: Any
)

data class Position(
  val id: Int,
  val unit: Int
)

data class Sms(
  val country: String,
  val number: String,
  val verified: Boolean
)