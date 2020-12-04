<template>
  <el-dialog
    :title="!dataForm.trainingAchieveId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="100px">
    <el-form-item label="基地名称" prop="trainingBaseName">
<!--      <el-input v-model="dataForm.trainingBaseName" placeholder="基地名称"></el-input>-->
      <el-select v-model="dataForm.trainingBaseName" placeholder="实训基地名称" >
        <el-option
          v-for="item in trainingnameList"
          :key="item.trainingBaseId"
          :label="item.trainingBaseName"
          :value="item.trainingBaseName">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="材料年度" prop="materialYear">
      <el-date-picker
        v-model="dataForm.materialYear"
        type="year"
        format="yyyy"
        value-format="yyyy"
        placeholder="材料年度">
      </el-date-picker>
    </el-form-item>
    <el-form-item label="材料类型" prop="materialType">
      <el-select v-model="dataForm.materialType" placeholder="材料类型" @change="changeelevel">
        <el-option
          v-for="item in trainingTypes"
          :key="item.materialTypeId"
          :label="item.trainingAchieveType"
          :value="item.trainingAchieveType">
        </el-option>
      </el-select>
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
          <span v-if="this.attachLists.length == 0" style="color: crimson">*请上传相关附件</span>
        </el-upload>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  class Trainingachment {
    constructor (file) {
      this.name = file.attachName
      this.url = file.attachPath
      this.file = file
    }
  }
  export default {
    data () {
      return {
        visible: false,
        url: '',
        fileAskContent: '无',
        fileIsNull: false,
        fileList: [],
        attachLists: [], // 附件列表
        delAttachLists: [], // 要删除的附件
        dataForm: {
          trainingAchieveId: 0,
          trainingBaseName: '',
          materialYear: '',
          materialType: '',
          materialTypeId: '',
          trainingBaseId: '',
          isDel: ''
        },
        textType: '',
        dataRule: {
          trainingBaseName: [
            { required: true, message: '基地名称不能为空', trigger: 'blur' }
          ],
          materialYear: [
            { required: true, message: '材料年度不能为空', trigger: 'blur' }
          ],
          materialType: [
            { required: true, message: '材料类型不能为空', trigger: 'blur' }
          ]
        },
        trainingTypes: [],
        trainingnameList: [] // 实训基地列表
      }
    },
    methods: {
      init (index, id) {
        this.url = this.$http.adornUrl(`/training/innovatetrainingbaseattach/upload?token=${this.$cookie.get('token')}`)
        this.dataForm.trainingAchieveId = id || 0
        this.visible = true
        this.getInstituteName()
        this.$http({
          url: this.$http.adornUrl(`/training/innovatetrainingbaseinfo/list`),
          method: 'get'
        }).then(({data}) => {
          this.trainingnameList = data.page.list
        })
        this.$http({
          url: this.$http.adornUrl(`/training/innovatetrainingachievetype/list`),
          method: 'get'
        }).then(({data}) => {
          this.trainingTypes = data.page.list
        })
        // eslint-disable-next-line eqeqeq
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.trainingAchieveId) {
            this.$http({
              url: this.$http.adornUrl(`/training/innovatetrainingbaseachieve/info/${this.dataForm.trainingAchieveId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.trainingBaseName = data.innovateTrainingBaseAchieveEntity.trainingBaseName
                this.dataForm.materialYear = data.innovateTrainingBaseAchieveEntity.materialYear
                this.dataForm.materialType = data.innovateTrainingBaseAchieveEntity.materialType
                this.dataForm.materialTypeId = data.innovateTrainingBaseAchieveEntity.materialTypeId
                this.dataForm.trainingBaseId = data.innovateTrainingBaseAchieveEntity.trainingBaseId
                this.dataForm.isDel = data.innovateTrainingBaseAchieveEntity.isDel
                this.attachLists = data.attachEntityList
                  // 附件回显
                let tempFinishAtta = []
                for (var i = 0; i < this.attachLists.length; i++) {
                  tempFinishAtta.push(new Trainingachment(this.attachLists[i]))
                }
                this.fileList = tempFinishAtta
              }
            })
          } else {
            this.attachLists = []
            this.fileList = []
            this.delAttachLists = []
          }
              // 获取文件要求：类型=>1 大创,2 中期检查,3 赛事,4 结题
          this.dataListLoading = true
          this.$http({
            url: this.$http.adornUrl(`/innovate/sys/file/ask/query`),
            method: 'get',
            params: this.$http.adornParams({
              'fileAskType': 10,
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
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid && this.attachLists.length > 0) {
            this.$http({
              url: this.$http.adornUrl(`/training/innovatetrainingbaseachieve/${!this.dataForm.trainingAchieveId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                trainingBaseAchieveEntity: this.dataForm,
                trainingBaseAttachList: this.attachLists,
                delBaseAttachList: this.delBaseAttachList
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
              }
            })
          }
        })
      },
      getInstituteName() {
        this.$http({
          url: this.$http.adornUrl(`/innovate/sys/institute/all`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.instituteName = data.institute
          }
        })
      },
      // 上传成功
      successHandle1 (response, file, fileList) {
        if (response && response.code === 0) {
          this.attachLists.push(response.trainingBaseAttachEntity)
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
            this.delAttachLists.push(this.attachLists[index])
          }
        }
        this.attachLists = tempFileList
      },
      changeelevel (val) {
        let list = this.trainingTypes.filter(item => {
          return item.trainingAchieveType.indexOf(val) > -1
        })
        this.dataForm.materialTypeId = list[0].materialTypeId
      }
    }
  }
</script>
