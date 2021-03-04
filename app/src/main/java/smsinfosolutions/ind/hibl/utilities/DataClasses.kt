package org.healthymantra.piousvision.utilities

import com.google.gson.annotations.SerializedName

/**
 * Created by Manish on 27-Jul-20.
 */


data class LovData(

    @SerializedName("Id") val id: String,
    @SerializedName("Name") val name: String
)

data class LovDataList(

    @SerializedName("data") val data: List<LovData>
)


data class AnimalTypeLov(

    @SerializedName("Id") val id: String,
    @SerializedName("Name") val name: String,
    @SerializedName("Limit") val limit: String
)

data class AnimalTypeLovList(

    @SerializedName("data") val data: List<AnimalTypeLov>
)

data class ProposalNo(

    @SerializedName("Name") val name: String
)

data class ProposalNoList(

    @SerializedName("data") val data: List<ProposalNo>
)


data class UserData(
    val proposal_no: String,
    val proposal_img: String?,
    val aadhar_no: String?,
    val aadhar_img: String?,
    val no_of_animal: String?,
    val category: String?,
    val area: String?,
    val duration: String?,
    val category_name: String?,
    val area_name: String?,
    val duration_name: String?,
    val wptd: String?,
    val city_id: String?,
    val city_name: String?,
    val hospital_name: String?,
    val hospital_id: String?,
    val age: String?


)

data class SendUserData(
    val proposal_no: String,
    val proposal_img: String?,
    val aadhar_no: String?,
    val aadhar_img: String?,
    val no_of_animal: String?,
    val category: String?,
    val area: String?,
    val duration: String?,
    val category_name: String?,
    val area_name: String?,
    val duration_name: String?,
    val wptd: String?,
    val city_id: String?,
    val city_name: String?,
    val hospital_name: String?,
    val hospital_id: String?,
    var check: Boolean,
    val age: String?


)


data class AnimalDetail(
    val proposal_no: String,
    val tag_no: String?,
    val animal_type: String?,
    val animal_type_code: String?,
    val sum_insured: String?,
)

data class AnimalImages(
    val proposal_no: String,
    val tag_no: String,
    val file_name: String,
    val file: String,

    )

data class PremiumDetail(
    @SerializedName("Proposal_no") val proposal_no: String,
    @SerializedName("Beneficiary_amt") val beneficiary_share: String,
    @SerializedName("Centre_amt") val central_share: String,
    @SerializedName("State_amt") val state_share: String,
    @SerializedName("Total_amt") val total_premius: String
)

data class PaymentDetail(
    @SerializedName("ProposalNo") val proposal_no: String,
    @SerializedName("TotalPayment") val total_payment: String,
    @SerializedName("Balance") val balance: String,
    var checked: Boolean = false
)

data class PaymentDetailList(
    @SerializedName("data") val payment_list: List<PaymentDetail>
)


data class CompletedFormDetail(
    @SerializedName("ProposalNo") val proposal_no: String,
    @SerializedName("Beneficiary_name") val beneficiary_name: String,
    @SerializedName("Use_Dt") val use_dt: String,
    @SerializedName("Inward") val inward: String,
    @SerializedName("Inward_Dt") val inward_dt: String,
    @SerializedName("NosOfAnimal") val no_of_animal: String,
    @SerializedName("TotalAmount") val total_amt: String,
    @SerializedName("CertificateNo") val certificate_no: String,
    @SerializedName("PolicyNo") val policy_no: String,
    @SerializedName("InsuranceFrom") val insurance_from: String,
)

data class CompletedFormList(
    @SerializedName("data") val form_list: List<CompletedFormDetail>
)

data class Claim(
    @SerializedName("TagNo") val tagNo : String,
    @SerializedName("Inti_Dt") val inti_Dt : String,
    @SerializedName("CoverNoteNo") val coverNoteNo : String,
    @SerializedName("CustName") val custName : String,
    @SerializedName("SumInsured") val sumInsured : String,
    @SerializedName("Investigator") val investigator : String,
    @SerializedName("InvestigatorMobile") val investigatorMobile : String,
    @SerializedName("Status") val status : String,
    @SerializedName("StatusDate") val statusDate : String)

data class ClaimList(
    @SerializedName("data") val claimList:List<Claim>
)

data class ClaimDetail(
    @SerializedName("STATUS") val status : String,
    @SerializedName("Remark") val remark : String,
    @SerializedName("U_EntDt") val end_dt : String
)

data class ClaimDetailList(
    @SerializedName("data") val claimDetailList:List<ClaimDetail>
)


data class PendingForm(
    val proposal_no: String
)

data class ShowAnimalDetail
    (
    @SerializedName("Animal") val animal: String,
    @SerializedName("TagNo") val tag_no: String,
    @SerializedName("SumInsured") val sum_insured: String?,
)

data class ShowAnimalDetailList(
    @SerializedName("data") val animal_list: List<ShowAnimalDetail>
)


