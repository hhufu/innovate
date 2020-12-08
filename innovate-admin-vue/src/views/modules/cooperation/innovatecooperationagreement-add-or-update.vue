<template>
  <el-dialog
    :title="!dataForm.enterpriseId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="企业名称" prop="enterpriseName">
      <el-select v-model="dataForm.enterpriseName" placeholder="企业名称" @change="changeenter">
        <el-option
          v-for="item in nameList"
          :key="item.authenticationId"
          :label="item.enterpriseName"
          :value="item.enterpriseName">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="二级学院" prop="instituteId">
      <el-select v-model="dataForm.instituteId" placeholder="二级学院">
      <el-option v-for="item in instituteList" :key="item.instituteName" :label="item.instituteName" :value="item.instituteId">
      </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="年度" prop="agreementYear">
      <el-date-picker
        v-model="dataForm.agreementYear"
        type="year"
        format="yyyy"
        value-format="yyyy"
        placeholder="选择年度">
      </el-date-picker>
    </el-form-item>
    <el-form-item label="协议时间" prop="agreementTime">
      <el-date-picker
        v-model="dataForm.agreementTime"
        type="date"
        value-format="yyyy-MM-dd HH:mm:ss"
        format="yyyy-MM-dd"
        placeholder="选择日期">
      </el-date-picker>
    </el-form-item>
    <el-form-item label="备注" prop="enterpriseRecords">
      <el-input v-model="dataForm.enterpriseRecords" placeholder="备注"></el-input>
    </el-form-item>
      <el-form-item label="附件要求">
        <template>
          <el-alert
            title=""
            type="success"
            :closable="false"
            :description="fileAskContent">
          </el-alert>
        </template>
      </el-form-item>
      <!--独立附件start-->
      <el-form-item label="相关附件" prop="reportSalesName">
        <el-upload
          class="upload-demo"
          :action="url"
          :data="{textType: textType}"
          :on-success="successHandle1"
          :on-remove="fileRemoveHandler"
          :file-list="fileList">
          <el-button size="small" type="primary">点击上传</el-button>
          <span v-if="attachLists.length === 0" style="color: crimson">*请上传相关附件</span>
        </el-upload>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()" :loading="loading" :disabled="loading">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  class Cooperationachment {
    constructor (file) {
      this.name = file.attachName
      this.url = file.attachPath
      this.file = file
    }
  }
  export default {
    data () {
      return {
        loading: false,
        visible: false,
        instituteList: [], // 学院列表
        url: '',
        fileAskContent: '无',
        fileIsNull: false,
        fileList: [],
        attachLists: [], // 附件列表
        delMaterialsList: [], // 要删除的附件
        dataForm: {
          enterpriseId: 0,
          enterpriseName: '',
          instituteId: '',
          agreementYear: '',
          agreementTime: '',
          agreementMaterials: '',
          enterpriseRecords: '',
          userId: ''
        },
        textType: '',
        dataRule: {
          enterpriseName: [
            { required: true, message: '企业名称不能为空', trigger: 'blur' }
          ],
          instituteId: [
            { required: true, message: '二级学院 innovate_sys_institute主键不能为空', trigger: 'blur' }
          ],
          agreementYear: [
            { required: true, message: '年度不能为空', trigger: 'blur' }
          ],
          agreementTime: [
            { required: true, message: '协议时间不能为空', trigger: 'blur' }
          ],
          agreementMaterials: [
            { required: true, message: '协议材料不能为空', trigger: 'blur' }
          ],
          userId: [
            { required: true, message: '创建者不能为空', trigger: 'blur' }
          ]
        },
        nameList: [] // 企业名称列表
      }
    },
    methods: {
      init (id) {
        this.dataForm.instituteId = this.$store.state.user.instituteId
        this.loading = false
        this.url = this.$http.adornUrl(`/cooperation/innovatecooperationmaterials/upload?token=${this.$cookie.get('token')}`)
        this.dataForm.enterpriseId = id || 0
        this.visible = true
        this.getInstituteList()
        this.$http({
          url: this.$http.adornUrl(`/cooperation/innovateregisterauthentication/list`),
          method: 'get',
          params: this.$http.adornParams({
            isDel: 0
          })
        }).then(({data}) => {
          this.nameList = data.page.list
        })
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.enterpriseId) {
            this.$http({
              url: this.$http.adornUrl(`/cooperation/innovatecooperationagreement/info/${this.dataForm.enterpriseId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                // this.dataForm = data.innovateCooperationAgreement
                this.dataForm.enterpriseName = data.innovateCooperationAgreement.enterpriseName
                this.dataForm.instituteId = data.innovateCooperationAgreement.instituteId
                this.dataForm.agreementYear = data.innovateCooperationAgreement.agreementYear
                this.dataForm.agreementTime = data.innovateCooperationAgreement.agreementTime
                this.dataForm.agreementMaterials = data.innovateCooperationAgreement.agreementMaterials
                this.dataForm.enterpriseRecords = data.innovateCooperationAgreement.enterpriseRecords
                this.dataForm.authenticationId = data.innovateCooperationAgreement.authenticationId
                this.dataForm.userId = data.innovateCooperationAgreement.userId
                this.attachLists = data.materialsEntityList
                // 附件回显
                let tempFinishAtta = []
                for (var i = 0; i < this.attachLists.length; i++) {
                  tempFinishAtta.push(new Cooperationachment(this.attachLists[i]))
                }
                this.fileList = tempFinishAtta
                this.isTeacherInfoNull()
                this.dataListLoading = false
              }
            }).catch((e) => {})  // length异常抛出
          } else {
            this.rest()
          }
          // 获取文件要求：类型=>1 大创,2 中期检查,3 赛事,4 结题
          this.dataListLoading = true
          this.$http({
            url: this.$http.adornUrl(`/innovate/sys/file/ask/query`),
            method: 'get',
            params: this.$http.adornParams({
              'fileAskType': 12,
              'fileAskTime': new Date().getFullYear()
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.fileAskContent = data.fileAsk == null ? '无' : data.fileAsk.fileAskContent
              this.dataListLoading = false
            }
          })
        })
      },
      rest() {
        this.attachLists = []
        this.fileList = []
        this.delMaterialsList = []
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid && this.attachLists.length > 0) {
            this.loading = true
            this.dataForm.userId = this.$store.state.user.id
            this.$http({
              url: this.$http.adornUrl(`/cooperation/innovatecooperationagreement/${!this.dataForm.enterpriseId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                cooperationAgreementEntity: this.dataForm,
                cooperationMaterialsList: this.attachLists,
                delMaterialsList: this.delMaterialsList,
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
                this.loading = false
              }
            })
          }
        })
      },
      getInstituteList () {
        this.$http({
          url: this.$http.adornUrl(`/innovate/sys/institute/all`),
          method: 'get'
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.instituteList = data.institute
          }
        })
      },
      // 上传成功
      successHandle1 (response, file, fileList) {
        if (response && response.code === 0) {
          this.attachLists.push(response.cooperationMaterialsEntity)
          this.fileIsNull = false
        } else {
          this.$message.error(response.msg)
        }
      },
      fileRemoveHandler (file, fileList) {
        // 移除attachList中的附件
        let tempFileList = []
        for (var index = 0; index < this.attachLists.length; index++) {
          if (this.attachLists[index].attachName !== file.name) {
            tempFileList.push(this.attachLists[index])
          } else {
            this.delMaterialsList.push(this.attachLists[index])
          }
        }
        this.attachLists = tempFileList
      },
      changeenter(val){
        let List = this.nameList.filter(item => {
          return item.enterpriseName.indexOf(val) > -1
        })
        this.dataForm.authenticationId = List[0].authenticationId
      }
    }
  }
</script>
