package com.h2.treebuds.client.models

data class TfPersonCard(
        val commandUuid: String,
        val entityReferenceTypeSummaries: List<EntityReferenceTypeSummary>,
        val facts: List<Fact>,
        val gender: Gender,
        val id: String,
        val lastModified: Long,
        val names: List<Name>,
        val pairings: List<Pairing>,
        val parentPairings: List<ParentPairing>,
        val summary: Summary,
        val version: Long
) {
  data class ParentPairing(
          val manRef: ManRef,
          val relationshipRefs: List<RelationshipRef>,
          val womanRef: WomanRef
  ) {
    data class ManRef(
            val personId: String
    )

    data class RelationshipRef(
            val lastModified: String,
            val lastUserModified: Long,
            val relationshipId: String,
            val relationshipType: String
    )

    data class WomanRef(
            val personId: String
    )
  }

  data class Fact(
          val attribution: Attribution,
          val commandUuid: String,
          val conclusionId: String,
          val conclusionType: String,
          val ctConclusionId: String,
          val schemaVersion: String,
          val value: Value,
          val valueHash: String
  ) {
    data class Value(
            val place: Place,
            val type: String
    ) {
      data class Place(
              val id: Long,
              val latitude: Double,
              val longitude: Double,
              val names: List<Name>,
              val original: String
      ) {
        data class Name(
                val lang: String,
                val value: String
        )
      }
    }

    data class Attribution(
            val agentApp: String,
            val contributor: Contributor,
            val modified: String,
            val modifiedDateNormalized: List<ModifiedDateNormalized>,
            val schemaVersion: String,
            val submitter: Submitter
    ) {
      data class Contributor(
              val id: String
      )

      data class ModifiedDateNormalized(
              val lang: String,
              val value: String
      )

      data class Submitter(
              val id: String
      )
    }
  }

  data class Summary(
          val canUserEdit: Boolean,
          val discussionsCount: Long,
          val gender: String,
          val initialCommandUuid: String,
          val lifespan: String,
          val lifespanBegin: LifespanBegin,
          val lifespanBeginYear: String,
          val lifespanEnd: LifespanEnd,
          val lifespanEndYear: String,
          val living: Boolean,
          val name: String,
          val nameForms: List<NameForm>,
          val readOnly: Boolean,
          val sourcesCount: Long,
          val spaceId: String,
          val tombstoned: Boolean
  ) {
    data class LifespanBegin(
            val date: Date?,
            val place: Place?,
            val type: String?
    ) {
      data class Date(
              val formal: String,
              val normalized: List<Normalized>,
              val original: String
      ) {
        data class Normalized(
                val lang: String,
                val value: String
        )
      }

      data class Place(
              val id: Long,
              val latitude: Double,
              val longitude: Double,
              val names: List<Name>,
              val original: String
      ) {
        data class Name(
                val lang: String,
                val value: String
        )
      }
    }

    data class NameForm(
            val ctScript: String,
            val fullText: String,
            val lang: String,
            val langtag: String,
            val order: String,
            val parts: List<Part>,
            val separator: String
    ) {
      data class Part(
              val type: String,
              val value: String
      )
    }

    data class LifespanEnd(
            val date: Date?,
            val place: Place?,
            val type: String?
    ) {
      data class Date(
              val formal: String,
              val normalized: List<Normalized>,
              val original: String
      ) {
        data class Normalized(
                val lang: String,
                val value: String
        )
      }

      data class Place(
              val id: Long,
              val latitude: Double,
              val longitude: Double,
              val names: List<Name>,
              val original: String
      ) {
        data class Name(
                val lang: String,
                val value: String
        )
      }
    }
  }

  data class Gender(
          val attribution: Attribution,
          val commandUuid: String,
          val conclusionId: String,
          val conclusionType: String,
          val ctConclusionId: String,
          val schemaVersion: String,
          val value: Value,
          val valueHash: String
  ) {
    data class Value(
            val type: String
    )

    data class Attribution(
            val agentApp: String,
            val contributor: Contributor,
            val migratedAttribution: String,
            val modified: String,
            val modifiedDateNormalized: List<ModifiedDateNormalized>,
            val schemaVersion: String,
            val submitter: Submitter
    ) {
      data class Contributor(
              val id: String
      )

      data class ModifiedDateNormalized(
              val lang: String,
              val value: String
      )

      data class Submitter(
              val id: String
      )
    }
  }

  data class EntityReferenceTypeSummary(
          val refs: List<Ref>,
          val type: String
  ) {
    data class Ref(
            val entityRefId: String,
            val role: String,
            val uri: String
    )
  }

  data class Name(
          val attribution: Attribution,
          val commandUuid: String,
          val conclusionId: String,
          val conclusionType: String,
          val ctConclusionId: String,
          val preferred: Boolean,
          val schemaVersion: String,
          val value: Value,
          val valueHash: String
  ) {
    data class Value(
            val ctStyle: String,
            val nameForms: List<NameForm>,
            val type: String
    ) {
      data class NameForm(
              val ctScript: String,
              val fullText: String,
              val lang: String,
              val langtag: String,
              val order: String,
              val parts: List<Part>,
              val separator: String
      ) {
        data class Part(
                val type: String,
                val value: String
        )
      }
    }

    data class Attribution(
            val agentApp: String,
            val contributor: Contributor,
            val migratedAttribution: String,
            val modified: String,
            val modifiedDateNormalized: List<ModifiedDateNormalized>,
            val schemaVersion: String,
            val submitter: Submitter
    ) {
      data class Contributor(
              val id: String
      )

      data class ModifiedDateNormalized(
              val lang: String,
              val value: String
      )

      data class Submitter(
              val id: String
      )
    }
  }

  data class Pairing(
          val children: List<Children>,
          val coupleFactValue: CoupleFactValue,
          val dismissedSuggestionSummaries: List<Any>,
          val hasBackingCoupleRelationship: Boolean,
          val manRef: ManRef,
          val relationshipRefs: List<RelationshipRef>,
          val womanRef: WomanRef
  ) {
    data class CoupleFactValue(
            val conclusionId: String,
            val date: Date,
            val place: Place,
            val type: String
    ) {
      data class Date(
              val formal: String,
              val normalized: List<Normalized>,
              val original: String
      ) {
        data class Normalized(
                val lang: String,
                val value: String
        )
      }

      data class Place(
              val id: Long,
              val latitude: Double,
              val longitude: Double,
              val names: List<Name>,
              val original: String
      ) {
        data class Name(
                val lang: String,
                val value: String
        )
      }
    }

    data class ManRef(
            val personId: String
    )

    data class Children(
            val personId: String,
            val relationshipRef: RelationshipRef
    ) {
      data class RelationshipRef(
              val lastModified: String,
              val lastUserModified: Long,
              val relationshipId: String,
              val relationshipType: String
      )
    }

    data class RelationshipRef(
            val lastModified: String,
            val lastUserModified: Long,
            val relationshipId: String,
            val relationshipType: String
    )

    data class WomanRef(
            val personId: String
    )
  }
}