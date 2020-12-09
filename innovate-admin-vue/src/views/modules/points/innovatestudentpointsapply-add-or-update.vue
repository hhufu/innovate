<template>
  <el-dialog
    :title="!dataForm.integralApplyId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()"
             label-width="100px">
      <el-form-item label="申请类型" prop="persionType">
        <el-select v-model="dataForm.participateType" @change="changeParticipate" style="width: 60%"
                   placeholder="请选择申报类型" default-first-option filterable clearable>
          <el-option v-for="item in participateList"
                     :label="item.participateType"
                     :value="item.participateType"
                     :key="item.applyIntegral"
          >

          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="项目名称" prop="raceGrade" v-if="raceGradeList.length > 0 || dataForm.raceGrade">
        <el-select v-model="dataForm.raceGrade" @change="changeRaceGrade" style="width: 60%" placeholder="请选择奖项等级"
                   default-first-option filterable clearable>
          <el-option v-for="item in raceGradeList"
                     :label="item.raceGrade"
                     :value="item.raceGrade"
                     :key="item.integralId"
          >

          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="奖项等级" prop="prizeGrade" v-if="prizeGradeList.length > 0 || dataForm.prizeGrade">
        <el-select v-model="dataForm.prizeGrade" @change="changePrizeGrade" style="width: 60%" placeholder="请选择奖项等级"
                   default-first-option filterable clearable>
          <el-option v-for="item in prizeGradeList"
                     :label="item.prizeGrade"
                     :value="item.prizeGrade"
                     :key="item.integralId"
          >

          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="参与人类别" prop="">
        <el-select v-model="dataForm.persionType" @change="changePersionType" placeholder="请选择参与人类别" clearable
                   filterable>
          <el-option v-for="item in persionTypeOptions"
                     :label="item.lable"
                     :value="item.value"
                     :key="item.value"
          >
          </el-option>
        </el-select>
        <div class="sub-title" style="color: red">注：/** 如果是参加活动培训或个人活动时可不填 **/</div>
      </el-form-item>
      <el-form-item label="申请积分" prop="applyIntegral">
        <el-input v-model="dataForm.applyIntegral" style="width: 200px" type="number" :disabled="noEditPoint"
                  placeholder="积分">
          <template slot="append">积分</template>
        </el-input>
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
          :data="{stuNum: $store.state.user.username}"
          :on-success="successHandle1"
          :on-remove="fileRemoveHandler"
          :file-list="fileList">
          <el-button size="small" type="primary">点击上传</el-button>
          <span v-if="fileList.length === 0" style="color: crimson">*请上传相关附件</span>
        </el-upload>
      </el-form-item>
      <!--独立附件end-->
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()" :loading="loading" :disabled="loading">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  // eslint-disable-next-line no-unused-vars
  class FinishAttachment {
    constructor(file) {
      this.name = file.attachName
      this.url = file.attachPath
      this.file = file
    }
  }

  export default {
    data() {
      return {
        loading: false,
        visible: false,
        attachLists: [], // 附件列表
        parentId: 0, // 父id
        integral: 0, // 该项目申请积分
        integralTo: 0, // 积分差值
        fileAskContent: '无',
        delAttachLists: [], // 要删除的附件
        url: '',
        noEditPoint: true,
        fileList: [],
        fileIsNull: false,
        participateList: [],
        raceGradeList: [],
        prizeGradeList: [],
        persionTypeOptions: [{lable: '负责人', value: 1}, {lable: '参与人员', value: 2}],
        dataForm: {
          integralApplyId: 0,
          applyTime: '',
          isDel: '',
          applyIntegral: '',
          persionType: '',
          prizeGrade: '',
          raceGrade: '',
          participateType: '',
          instituteId: 0,
          sysPointsId: 0,
          applyUserId: 0,
          stuNum: ''
        },
        textType: '',
        dataRule: {
          applyTime: [
            {required: true, message: '申请时间不能为空', trigger: 'blur'}
          ],
          applyIntegral: [
            {required: true, message: '积分不能为空', trigger: 'blur'}
          ],
          raceGrade: [
            {required: true, message: '项目名称不能为空', trigger: 'blur'}
          ],
          prizeGrade: [
            {required: true, message: '奖项等级不能为空', trigger: 'blur'}
          ],
          participateType: [
            {required: true, message: '参与人类别不能为空', trigger: 'blur'}
          ],
        }
      }
    },
    methods: {
      init(id) {
        this.loading = false
        this.url = this.$http.adornUrl(`/points/attach/upload?token=${this.$cookie.get('token')}`)
        this.dataForm.integralApplyId = id || 0
        this.getTypeList()
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.integralApplyId) {
            this.$http({
              url: this.$http.adornUrl(`/points/innovatestudentpointsapply/info/${this.dataForm.integralApplyId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.applyTime = data.innovateStudentPointsApply.applyTime
                this.dataForm.isDel = data.innovateStudentPointsApply.isDel
                this.dataForm.applyIntegral = data.innovateStudentPointsApply.applyIntegral
                this.dataForm.persionType = data.innovateStudentPointsApply.persionType
                this.dataForm.instituteId = data.innovateStudentPointsApply.instituteId
                this.dataForm.prizeGrade = data.innovateStudentPointsApply.prizeGrade
                this.dataForm.raceGrade = data.innovateStudentPointsApply.raceGrade
                this.dataForm.stuNum = data.innovateStudentPointsApply.stuNum
                this.dataForm.participateType = data.innovateStudentPointsApply.participateType
                this.dataForm.applyUserId = data.innovateStudentPointsApply.applyUserId
                this.attachLists = data.attachEntityList
                // 附件回显
                let tempFinishAtta = []
                for (var i = 0; i < this.attachLists.length; i++) {
                  tempFinishAtta.push(new FinishAttachment(this.attachLists[i]))
                }
                this.fileList = tempFinishAtta
                // 数据修改回显
                this.changeParticipate(this.dataForm.participateType)
                this.changeRaceGrade(data.innovateStudentPointsApply.raceGrade)
                this.dataForm.raceGrade = data.innovateStudentPointsApply.raceGrade
                this.dataForm.prizeGrade = data.innovateStudentPointsApply.prizeGrade
                this.dataForm.sysPointsId = data.innovateStudentPointsApply.sysPointsId
              }
            })
          } else {
            this.reset();
          }
        })
      },
      // 重置
      reset() {
        this.dataForm = {
          integralApplyId: 0,
          applyTime: '',
          isDel: '',
          applyIntegral: '',
          persionType: '',
          prizeGrade: '',
          raceGrade: '',
          participateType: '',
          sysPointsId: 0,
          applyUserId: 0,
          instituteId: 0,
          stuNum: ''
        }
        this.attachLists = []
        this.fileList = []
        this.fileAskContent = '无'
        this.delAttachLists = []
        this.participateList = []
        this.raceGradeList = []
        this.prizeGradeList = []
      },
      getTypeList() {
        this.participateList = []
        this.$http({
          url: this.$http.adornUrl('/points/innovatesyspoints/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 1000,
            'parentId': 0,
            'isDel': 0
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.participateList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.participateList = []
            this.totalPage = 0
          }
        })
      },
      // 表单提交
      dataFormSubmit() {
        this.$refs['dataForm'].validate((valid) => {
          if (valid && this.attachLists.length > 0) {
            this.loading = true
            this.dataForm.applyUserId = this.$store.state.user.id
            this.dataForm.instituteId = this.$store.state.user.instituteId
            this.dataForm.stuNum = this.$store.state.user.username
            this.$http({
              url: this.$http.adornUrl(`/points/innovatestudentpointsapply/${!this.dataForm.integralApplyId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                pointsApplyEntity: this.dataForm,
                pointsAttachEntityList: this.attachLists,
                delAttachLists: this.delAttachLists
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
      // 选择申报类型
      changeParticipate(e) {
        // eslint-disable-next-line no-unused-vars
        let list = this.participateList.filter(item => {
          return item.participateType.indexOf(e) > -1
        })
        this.parentId = list[0].integralId
        this.fileAskContent = list[0].fileAskContent || '无'
        // 重置
        this.dataForm.raceGrade = ''
        this.dataForm.prizeGrade = ''
        this.dataForm.sysPointsId = 0
        this.raceGradeList = []
        this.prizeGradeList = []
        // 积分规则--项目名称
        this.$http({
          url: this.$http.adornUrl('/points/innovatesyspoints/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 50,
            'groupBy': 'raceGrade',
            'parentId': this.parentId,
            'isDel': 0
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.raceGradeList = data.page.list
          } else {
            this.raceGradeList = []
          }
        })
      },
      // 选择项目名称
      changeRaceGrade(e) {
        // 重置
        this.dataForm.prizeGrade = ''
        this.dataForm.sysPointsId = 0
        this.prizeGradeList = []
        // 积分规则--奖项等级
        this.$http({
          url: this.$http.adornUrl('/points/innovatesyspoints/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 50,
            'key': 'race_grade',
            'keyValue': e,
            'groupBy': 'prizeGrade',
            prizeGradeIsNull: 'true',
            'parentId': this.parentId,
            'isDel': 0
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.prizeGradeList = data.page.list
            // eslint-disable-next-line eqeqeq
            if (data.page.list.length == 0) {
              this.prizeGradeList = []
              let list = this.raceGradeList.filter(item => {
                // eslint-disable-next-line eqeqeq
                return item.raceGrade.indexOf(e) > -1
              })
              this.dataForm.applyIntegral = list[0].integral // 获取对应积分
              this.integral = list[0].integral
              this.integralTo = list[0].integralTo
              this.dataForm.sysPointsId = list[0].integralId
              // 改变已选人员类别积分
              this.personChickChange()
            }
          } else {
            this.prizeGradeList = []
          }
        })
      },
      // 选择--奖项等级
      changePrizeGrade(e) {
        let list = this.prizeGradeList.filter(item => {
          // eslint-disable-next-line eqeqeq
          return item.prizeGrade === e
        })
        // 获取对应积分
        this.dataForm.applyIntegral = list[0].integral
        this.integral = list[0].integral
        this.integralTo = list[0].integralTo
        this.dataForm.sysPointsId = list[0].integralId
        // 改变已选人员类别积分
        this.personChickChange()
      },
      // 选择人员类别
      changePersionType(e) {
        // eslint-disable-next-line eqeqeq
        if (e == 1) {
          // 负责人积分
          this.dataForm.applyIntegral = this.integral
          // eslint-disable-next-line eqeqeq
        } else if (e == 2) {
          // 参与人员积分
          this.dataForm.applyIntegral = this.integral - this.integralTo
        }
      },
      personChickChange() {
        // eslint-disable-next-line eqeqeq
        if (this.dataForm.persionType === 1) {
          // 负责人积分
          this.dataForm.applyIntegral = this.integral
          // eslint-disable-next-line eqeqeq
        } else if (this.dataForm.persionType === 2) {
          // 参与人员积分
          this.dataForm.applyIntegral = this.integral - this.integralTo
        }
      },
      // 上传成功
      successHandle1(response, file, fileList) {
        if (response && response.code === 0) {
          this.attachLists.push(response.pointsAttachEntity)
          this.fileIsNull = false
        } else {
          this.$message.error(response.msg)
        }
      },
      fileRemoveHandler(file, fileList) {
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
      }
    }
  }
</script>
