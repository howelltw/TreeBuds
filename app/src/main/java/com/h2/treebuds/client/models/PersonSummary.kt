package com.h2.treebuds.client.models

import com.squareup.moshi.Json
import java.util.*

/**
 * A summary of the opportunities that exist for a Tree Person.
 */
class PersonSummary {
  /**
   * Unique identifier of the given person summary.
   */
  var id: String? = null
  /**
   * Unique Tree ID of the person who the summary is for.
   */
  var personId: String
  private var hasTemple: Boolean = false
  /**
   * Priority Score of the Temple Opportunity
   */
  var templePriority: Int = 0
  /**
   * If the person has a temple opportunity, this contains the temple type. Can be "READY", "NEEDS_MORE_INFORMATION", or "DUPLICATE_PERSON."
   * @return A string stating the temple type if the person has a temple opportunity. Null otherwise.
   */
  var templeType: String? = null
  private var hasRecord: Boolean = false
  /**
   * Raw score representing the person's highest record hint priority score from their opportunities.
   * @return Integer representing their highest record hint opportunities score.
   */
  var recordPriority: Int = 0
  /**
   * Map of Record Types and their corresponding resource uris that represent the Record Hints currently matched against the given person<br></br>
   * {"NEW_PERSON": [], "NEW_CONCLUSION": ["LVBZ-RLGH"], "NEW_PERSON": ["LVBZ-RLGH", "HGFR-PLLK"], "OTHER": []}
   * @return List of Record Types associated with the Person's Record Hints
   */
  var recordTypes: Map<String, List<String>>? = null

  @Json(name = "prioritySoiSources")
  private var prioritySoiSources: MutableList<String>? = null
  /**
   * A unix timestamp representing the time the opportunity was created.
   */
  var createdTime: Long = 0
    internal set
  /**
   * A unix timestamp representing the time the opportunity was modified last.
   */
  var lastModified: Long = 0
    internal set
  /**
   * An implementation of Weighted Relationship Distance algorithm, created by Ben Baker.
   */
  var closeness = java.lang.Double.MAX_VALUE

  //Constructors
  internal constructor() {
    id = ""
    personId = ""
    hasTemple = false
    templeType = null
    hasRecord = false
    recordTypes = HashMap()
    prioritySoiSources = ArrayList()
  }

  // Simple object that requires a large number of parameters. This does not increase complexity nor decrease readability.
  constructor(id: String,
              personId: String,
              hasTemple: Boolean,
              templePriority: Int,
              templeType: String?,
              hasRecord: Boolean,
              recordPriority: Int,
              resourceUrisByRecordType: Map<String, List<String>>,
              soiSources: List<String>,
              createdTime: Long,
              lastModified: Long) {
    this.id = id
    this.personId = personId
    this.hasTemple = hasTemple
    this.templePriority = templePriority
    this.templeType = templeType
    this.hasRecord = hasRecord
    this.recordPriority = recordPriority
    this.recordTypes = HashMap(resourceUrisByRecordType)
    this.prioritySoiSources = ArrayList(soiSources)
    this.createdTime = createdTime
    this.lastModified = lastModified
  }

  /**
   * Boolean value indicating if the person has any temple opportunities available.
   * @return Whether or not the person has any Temple Opportunities
   */
  fun hasTemple(): Boolean {
    return hasTemple
  }

  fun hasTemple(hasTemple: Boolean) {
    this.hasTemple = hasTemple
  }

  /**
   * Boolean value indicating if the person has any record opportunities available.
   */
  fun hasRecord(): Boolean {
    return hasRecord
  }

  fun hasRecord(hasRecord: Boolean) {
    this.hasRecord = hasRecord
  }

  /**
   * List of SOI person sources that represent the information that was used to calculate the priority<br></br>
   * This can have multiple or single values in the array ["PEDIGREE", "VISITED"] or ["PEDIGREE"]
   * @return List of Priority Sources associated with the Person's SOI Source
   */
  fun getSoiSources(): MutableList<String>? {
    return prioritySoiSources
  }

  fun setSoiSources(soiSources: MutableList<String>) {
    this.prioritySoiSources = soiSources
  }

  internal fun appendSoiSource(soiSource: String) {
    if (!this.prioritySoiSources!!.contains(soiSource)) {
      this.prioritySoiSources!!.add(soiSource)
    }
  }

  // Override Methods
  override// "Complex" conditional statement is easy to read
  fun equals(o: Any?): Boolean {
    if (this === o) {
      return true
    }

    if (o !is PersonSummary) {
      return false
    }

    val that = o as PersonSummary?
    return hasTemple == that!!.hasTemple &&
            hasRecord == that.hasRecord &&
            recordPriority == that.recordPriority &&
            createdTime == that.createdTime &&
            lastModified == that.lastModified &&
            id == that.id &&
            personId == that.personId &&
            templeType == that.templeType &&
            prioritySoiSources == that.prioritySoiSources &&
            recordTypes == that.recordTypes
  }

  override fun hashCode(): Int {
    return Objects.hash(id, personId, hasTemple, templePriority, templeType, hasRecord, recordPriority, recordTypes, prioritySoiSources, createdTime, lastModified)
  }

  override fun toString(): String {
    val sb = StringBuilder("PersonSummary{")
    sb.append("id='").append(id).append('\'')
    sb.append(", personId='").append(personId).append('\'')
    sb.append(", hasTemple=").append(hasTemple)
    sb.append(", templePriority=").append(templePriority)
    sb.append(", templeType='").append(templeType).append('\'')
    sb.append(", hasRecord=").append(hasRecord)
    sb.append(", recordPriority=").append(recordPriority)
    sb.append(", recordTypes=").append(recordTypes)
    sb.append(", prioritySoiSources=").append(prioritySoiSources)
    sb.append(", createdTime=").append(createdTime)
    sb.append(", lastModified=").append(lastModified)
    sb.append('}')
    return sb.toString()
  }
}
