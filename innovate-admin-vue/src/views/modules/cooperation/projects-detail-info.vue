<template>
  <el-dialog
    title="详情"
    :close-on-click-modal="true"
    :visible.sync="visible"
    :append-to-body="true"
  >
    <table border="1" cellspacing="0" width="100%" class="table">
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr class="contents" align="center">
        <th colspan="10" style="height: 3.2rem">
          企业合作项目信息
        </th>
      </tr>
      <tr align='center'>
        <th colspan="2">主持人</th>
        <td colspan="8">{{dataForm.userId}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">企业名称</th>
        <td colspan="8">{{dataForm.enterpriseName}}</td>
      </tr>
      <tr align="center">
        <th colspan="2">项目名称</th>
        <td colspan="8">{{dataForm.projectName}}</td>
      </tr>
<!--      <tr align='center' v-if="dataForm.persionType != '' && dataForm.persionType != null">-->
<!--        <th colspan="2">参与人类别</th>-->
<!--        <td colspan="8">{{dataForm.persionType? (dataForm.persionType == 1 ?'负责人':'参与成员'): '-'}}</td>-->
<!--      </tr>-->
      <tr align='center' >
        <th colspan="2">二级学院</th>
        <td colspan="8">{{dataForm.instituteId}}</td>
      </tr>
      <tr align='center' >
        <th colspan="2">年度</th>
        <td colspan="8">{{dataForm.cooperationYear}}年</td>
      </tr>
      <tr align='center'>
        <th colspan="2">起始时间</th>
        <td colspan="8">{{parseTime(dataForm.startTime, '{y}年{m}月{d}日')}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">截止时间</th>
        <td colspan="8">{{parseTime(dataForm.endTime, '{y}年{m}月{d}日')}}</td>
      </tr>
      <!--附件开始-->
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr align="center" class="contents">
        <th colspan="10" style="height: 3.2rem">附件</th>
      </tr>
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr align='center'>
        <th colspan="7">附件名</th>
        <td colspan="3">操作</td>
      </tr>
      <template>
        <tr v-for="item in attachLists"
            align="center">
          <td colspan="7">{{item.attachName}}</td>
          <td colspan="3"><el-button @click="attachDown(item)" :loading="downloadLoading"><span v-text="downloadText"></span></el-button></td>
        </tr>
      </template>
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <!--附件结束-->
    </table>
  </el-dialog>
</template>

<script>
    export default {
      data () {
        return {
          visible: false,
          downloadLoading: false,
          downloadText: '下载',
          userEntity: {}, // 用户信息
          participateList: [],
          url: '',
          fileAskContent: '无',
          fileIsNull: true,
          fileList: [],
          attachLists: [], // 附件列表
          delAttachLists: [], // 要删除的附件
          dataForm: {
            cooperationId: 0,
            projectName: '',
            instituteId: '',
            enterpriseName: '',
            cooperationYear: '',
            userId: '',
            startTime: '',
            endTime: ''
          },
          textType: ''
        }
      },
      methods: {
        init (id) {
          this.url = this.$http.adornUrl(`/cooperation/innovatecooperationmaterials/upload?token=${this.$cookie.get('token')}`)
          this.dataForm.cooperationId = id || 0
          this.visible = true
          // this.getInstituteList()
          this.$nextTick(() => {
            if (this.dataForm.cooperationId) {
              this.$http({
                url: this.$http.adornUrl(`/cooperation/innovatecooperationprojects/info/${this.dataForm.cooperationId}`),
                method: 'get',
                params: this.$http.adornParams()
              }).then(({data}) => {
                if (data && data.code === 0) {
                  // this.dataForm = data.innovateCooperationProjects
                  this.dataForm.projectName = data.innovateCooperationProjectsEntity.projectName
                  this.dataForm.instituteId = data.innovateCooperationProjectsEntity.instituteId
                  this.dataForm.enterpriseName = data.innovateCooperationProjectsEntity.enterpriseName
                  this.dataForm.cooperationYear = data.innovateCooperationProjectsEntity.cooperationYear
                  this.dataForm.userId = data.innovateCooperationProjectsEntity.userId
                  this.dataForm.startTime = data.innovateCooperationProjectsEntity.startTime
                  this.dataForm.endTime = data.innovateCooperationProjectsEntity.endTime
                  this.dataForm.userId = data.innovateCooperationProjectsEntity.userId
                  this.attachLists = data.materialsEntityList
                }
              })
            } else {
              this.reset()
            }
          })
        },
        // 文件下载
        attachDown (attach) {
          this.downloadLoading = true
          this.downloadText = '正在下载'
          this.$notify({
            title: '下载提示',
            message: '文件正在后台下载,请您稍后!',
            duration: 0,
            type: 'success'
          })
          this.$httpFile({
            url: this.$httpFile.adornUrl(`/cooperation/innovatecooperationmaterials/download`),
            method: 'post',
            params: this.$httpFile.adornParams({
              'filePath': attach.attachPath
            })
          }).then(response => {
            if (!response) {
              this.$notify({
                title: '下载提示',
                message: '下载失败',
                duration: 0,
                type: 'error'
              })
              this.downloadLoading = false
              return
            }
            let url = window.URL.createObjectURL(new Blob([response.data]))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', attach.attachName)
            document.body.appendChild(link)
            link.click()
            this.$notify({
              title: '下载成功',
              message: '后台文件下载成功',
              duration: 0,
              type: 'success'
            })
            this.downloadText = '下载'
            this.downloadLoading = false
          }).catch(err => {
            console.log(err)
            this.downloadLoading = false
          })
        }
      }
    }
</script>

<style scoped>

</style>
