<template>
  <el-dialog
    title="详情"
    :close-on-click-modal="true"
    :visible.sync="visible"
    :append-to-body="true"
  >
    <table border="1" cellspacing="0" width="100%" class="table" style="table-layout: fixed">
      <tr align='center'>
        <td colspan="10" style="height: 1.2rem"></td>
      </tr>
      <tr class="contents" align="center">
        <th colspan="10" style="height: 3.2rem">
          企业成果信息
        </th>
      </tr>
      <tr align='center'>
        <th colspan="2">负责人</th>
        <td colspan="8">{{dataForm.enterpriseDirector}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">企业名称</th>
        <td colspan="8">{{dataForm.enterpriseName}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">二级学院</th>
        <td colspan="8">{{dataForm.instituteId}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">获奖名称（项目名称）</th>
        <td colspan="8">{{dataForm.awardProjectName}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">获奖（得）时间</th>
        <td colspan="8">{{parseTime(dataForm.awardTime, '{y}年{m}月{d}日')}}</td>
      </tr>
      <tr align='center'>
        <th colspan="2">类型</th>
        <td colspan="8">{{dataForm.awardProjectType}}</td>
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
          <td colspan="3">
            <el-button @click="attachDown(item)" :loading="downloadLoading"><span v-text="downloadText"></span>
            </el-button>
          </td>
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
          downloadLoading: false,
          downloadText: '下载',
          visible: false,
          dataForm: {
            enterpAchieveId: 0,
            enterpriseId: '',
            enterpriseName: '',
            enterpriseDirector: '',
            enterpriseUserId: '',
            awardProjectName: '',
            awardTime: '',
            awardProjectType: '',
            instituteId: '',
            isDel: '',
            disabled: ''
          },
          projectName: [],
          instituteName: [],
          fileList: [],
          attachLists: [],
          url: ''
        }
      },
      methods: {
        init (id) {
          this.dataForm.enterpAchieveId = id || 0
          this.visible = true
          this.$nextTick(() => {
            if (this.dataForm.enterpAchieveId) {
              this.disabled = true
              this.$http({
                url: this.$http.adornUrl(`/enterprise/innovateenterpriseachieve/info/${this.dataForm.enterpAchieveId}`),
                method: 'get',
                params: this.$http.adornParams()
              }).then(({data}) => {
                if (data && data.code === 0) {
                  this.dataForm.enterpriseId = data.achieveModel.achieveEntity.enterpriseId
                  this.dataForm.enterpriseName = data.achieveModel.achieveEntity.enterpriseName
                  this.dataForm.enterpriseDirector = data.achieveModel.achieveEntity.enterpriseDirector
                  this.dataForm.enterpriseUserId = data.achieveModel.achieveEntity.enterpriseUserId
                  this.dataForm.awardProjectName = data.achieveModel.achieveEntity.awardProjectName
                  this.dataForm.awardTime = data.achieveModel.achieveEntity.awardTime
                  this.dataForm.awardProjectType = data.achieveModel.achieveEntity.typeName
                  this.dataForm.instituteId = data.achieveModel.achieveEntity.instituteName
                  this.dataForm.isDel = data.achieveModel.achieveEntity.isDel
                  this.attachLists = data.achieveModel.attachEntities
                }
              })
            }
          })
        },
        // 文件下载
        attachDown (attach) {
          debugger
          this.downloadLoading = true
          this.downloadText = '正在下载'
          this.$notify({
            title: '下载提示',
            message: '文件正在后台下载,请您稍后!',
            duration: 0,
            type: 'success'
          })
          this.$httpFile({
            url: this.$httpFile.adornUrl(`/enterprise/innovateenterpriseattach/download`),
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
            this.downloadLoading = false
          })
        }
      }
    }
</script>

<style scoped>

</style>
